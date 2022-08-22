package dev.brella.kornea.fx.base

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap

/** Inline Insanity */

public inline fun <E> baseListChangeListener(
    crossinline onPermutations: (ListChangeListener.Change<out E>) -> Unit = {},
    crossinline onUpdates: (ListChangeListener.Change<out E>) -> Unit = {},
    crossinline onRemoved: (ListChangeListener.Change<out E>) -> Unit = {},
    crossinline onAdded: (ListChangeListener.Change<out E>) -> Unit = {},
): ListChangeListener<E> =
    ListChangeListener { change ->
        while (change.next()) {
            if (change.wasPermutated()) {
                onPermutations(change)
            } else if (change.wasUpdated()) {
                onUpdates(change)
            } else {
                onRemoved(change)
                onAdded(change)
            }
        }
    }

public inline fun <E> listChangeListener(
    hasPermutations: Boolean = false,
    crossinline onPermutations: (ObservableList<out E>, movedFrom: Int, movedTo: Int) -> Unit = { _, _, _ -> },
    hasUpdates: Boolean = false,
    crossinline onUpdates: (ObservableList<out E>, updatedInterval: IntRange) -> Unit = { _, _ -> },
    hasRemoved: Boolean = false,
    crossinline onRemoved: (ObservableList<out E>, removed: List<E>, removedAt: Int) -> Unit = { _, _, _ -> },
    hasAdded: Boolean = false,
    crossinline onAdded: (ObservableList<out E>, added: List<E>, addedInterval: IntRange) -> Unit = { _, _, _ -> },
): ListChangeListener<E> =
    if (hasPermutations) {
        if (hasUpdates) {
            if (hasRemoved) {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (change.wasUpdated()) {
                                onUpdates(list, change.from until change.to)
                            } else {
                                onRemoved(list, change.removed, change.from)
                                onAdded(list, change.addedSubList, change.from until change.to)
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (change.wasUpdated()) {
                                onUpdates(list, change.from until change.to)
                            } else {
                                onRemoved(list, change.removed, change.from)
                            }
                        }
                    }
                }
            } else {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (change.wasUpdated()) {
                                onUpdates(list, change.from until change.to)
                            } else {
                                onAdded(list, change.addedSubList, change.from until change.to)
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (change.wasUpdated()) {
                                onUpdates(list, change.from until change.to)
                            }
                        }
                    }
                }
            }
        } else {
            if (hasRemoved) {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (!change.wasUpdated()) {
                                onRemoved(list, change.removed, change.from)
                                onAdded(list, change.addedSubList, change.from until change.to)
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (!change.wasUpdated()) {
                                onRemoved(list, change.removed, change.from)
                            }
                        }
                    }
                }
            } else {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            } else if (!change.wasUpdated()) {
                                onAdded(list, change.addedSubList, change.from until change.to)
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (change.wasPermutated()) {
                                for (i in change.from until change.to) {
                                    onPermutations(list, i, change.getPermutation(i))
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        if (hasUpdates) {
            if (hasRemoved) {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (change.wasUpdated()) {
                                    onUpdates(list, change.from until change.to)
                                } else {
                                    onRemoved(list, change.removed, change.from)
                                    onAdded(list, change.addedSubList, change.from until change.to)
                                }
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (change.wasUpdated()) {
                                    onUpdates(list, change.from until change.to)
                                } else {
                                    onRemoved(list, change.removed, change.from)
                                }
                            }
                        }
                    }
                }
            } else {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (change.wasUpdated()) {
                                    onUpdates(list, change.from until change.to)
                                } else {
                                    onAdded(list, change.addedSubList, change.from until change.to)
                                }
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (change.wasUpdated()) {
                                    onUpdates(list, change.from until change.to)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (hasRemoved) {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (!change.wasUpdated()) {
                                    onRemoved(list, change.removed, change.from)
                                    onAdded(list, change.addedSubList, change.from until change.to)
                                }
                            }
                        }
                    }
                } else {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (!change.wasUpdated()) {
                                    onRemoved(list, change.removed, change.from)
                                }
                            }
                        }
                    }
                }
            } else {
                if (hasAdded) {
                    ListChangeListener { change ->
                        val list = change.list

                        while (change.next()) {
                            if (!change.wasPermutated()) {
                                if (!change.wasUpdated()) {
                                    onAdded(list, change.addedSubList, change.from until change.to)
                                }
                            }
                        }
                    }
                } else {
                    ListChangeListener {}
                }
            }
        }
    }



