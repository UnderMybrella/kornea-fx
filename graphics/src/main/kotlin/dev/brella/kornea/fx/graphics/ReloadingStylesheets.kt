package dev.brella.kornea.fx.graphics

import dev.brella.kornea.fx.base.replaceAllFirstOrAdd
import dev.brella.kornea.fx.base.replaceFirstOrAdd
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.scene.Scene
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.File
import java.net.URI
import java.nio.file.*
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.toPath

open class ReloadingStylesheets(val uris: Set<URI>, ioScope: CoroutineScope) : Closeable, CoroutineScope {
    protected val scenes: ObservableList<Scene> = FXCollections.observableArrayList()

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + ioScope.coroutineContext

    public fun addScene(scene: Scene) {
        this.scenes.add(scene)
        reload(uris.map(URI::toString), scene)
    }

    public fun removeScene(scene: Scene) {
        this.scenes.remove(scene)

        Platform.runLater { scene.stylesheets.removeAll(uris.map(URI::toString)) }
    }

    public fun reload(uri: URI) =
        reload(uri.toString(), scenes)

    public fun reload(uris: Collection<URI>) =
        reload(uris.map(URI::toString), scenes)

    public fun reload(vararg uris: URI) =
        reload(uris.map(URI::toString), scenes)

    protected inline fun reload(uris: List<String>, scenes: Iterable<Scene>) =
        Platform.runLater { scenes.forEach { scene -> reload(uris, scene) } }

    protected inline fun reload(uris: List<String>, scene: Scene) =
        scene.stylesheets.replaceAllFirstOrAdd(uris, uris) { a, b -> URI(a) == URI(b) }


    protected inline fun reload(uri: String, scenes: Iterable<Scene>) =
        Platform.runLater { scenes.forEach { scene -> reload(uri, scene) } }

    protected inline fun reload(uri: String, scene: Scene) =
        scene.stylesheets.replaceFirstOrAdd(uri, uri) { a, b -> URI(a) == URI(b) }

    @Suppress("UNCHECKED_CAST")
    protected open fun onFileSystemEvent(key: WatchKey, event: WatchEvent<*>) {
        when (event.kind()) {
            StandardWatchEventKinds.ENTRY_MODIFY -> onFileModified(key, event as? WatchEvent<Path> ?: return)
        }
    }

    protected open fun onFileModified(key: WatchKey, event: WatchEvent<Path>) {
        val modified = (key.watchable() as? Path ?: return)
            .relativize(event.context())
            .toUri()

        if (modified in uris) reload(modified)
    }

    override fun close() {
        cancel()
    }

    init {
        uris.forEach(this::reload)

        uris.mapNotNull { uri ->
            try {
                when (uri.scheme) {
                    "file" -> FileSystems.getFileSystem(
                        URI(
                            uri.scheme,
                            uri.userInfo,
                            uri.host,
                            uri.port,
                            "/",
                            null,
                            null
                        )
                    ) to uri

                    else -> FileSystems.getFileSystem(uri) to uri
                }
            } catch (th: Throwable) {
                th.printStackTrace()
                null
            }
        }
            .groupBy(Pair<FileSystem, URI>::first, Pair<FileSystem, URI>::second)
            .forEach { (fs, uris) ->
                val watchService = fs.newWatchService()
                uris.mapTo(HashSet()) { uri -> uri.toPath().parent }
                    .forEach { path -> path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY) }

                launch {
                    watchService.use { watcher ->
                        while (isActive) {
                            val key = watcher.poll()
                            if (key == null) {
                                delay(500)
                                continue
                            }

                            try {
                                key.pollEvents().forEach { event -> onFileSystemEvent(key, event) }
                            } catch (th: Throwable) {
                                th.printStackTrace()
                            } finally {
                                key.reset()
                                delay(100)
                            }
                        }
                    }
                }
            }
    }
}