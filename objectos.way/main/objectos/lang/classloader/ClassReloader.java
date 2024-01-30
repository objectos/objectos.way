/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * This file is part of the objectos.www project.
 *
 * objectos.www is NOT free software and is the intellectual property of Objectos Software LTDA.
 *
 * Source is available for educational purposes only.
 */
package objectos.lang.classloader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchService;
import objectos.notes.NoteSink;
import objectox.lang.classloader.ClassReloaderBuilder;

/**
 * Reloads an specified class and its dependencies if changes were observed in
 * configured directories.
 */
public sealed interface ClassReloader
    extends AutoCloseable permits objectox.lang.classloader.ClassReloaderImpl {

  /**
   * A builder for {@link ClassReloader} objects.
   */
  sealed interface Builder permits objectox.lang.classloader.ClassReloaderBuilder {

    /**
     * Sets the note sink instance.
     *
     * @param noteSink
     *        the note sink instance
     *
     * @return this builder
     */
    Builder noteSink(NoteSink noteSink);

    /**
     * Sets the {@link WatchService} instance.
     *
     * @param service
     *        the watch service instance
     * 
     * @return this builder
     */
    Builder watchService(WatchService service);

    /**
     * Watches the specified directory for changes and recursively looks for
     * {@code .class} files in it for loading classes whose binary name
     * starts with the specified {@code prefix}.
     *
     * @param directory
     *        the directory to be watched
     * @param prefix
     *        only loads classes whose binary name starts with this prefix
     *
     * @return this builder
     *
     * @throws IllegalArgumentException
     *         if the specified path is not of a directory
     */
    Builder watch(Path directory, String prefix);

    /**
     * Creates a {@code ClassReloader} instance for reloading the class with the
     * specified binary name from the current state of this builder.
     *
     * @param binaryName
     *        the binary name of the class
     *
     * @return a new {@code ClassReloader} instance from the current state of
     *         this builder
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    ClassReloader of(String binaryName) throws IOException;

  }

  /**
   * Returns a new builder of {@code ClassReloader} objects.
   *
   * @return a newly created builder instance
   */
  static Builder builder() {
    return new ClassReloaderBuilder();
  }

  /**
   * Closes this class reloader and any associated file system watcher.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  void close() throws IOException;

  /**
   * Returns the class object representing the class of this reloader that is in
   * sync with any file system change.
   *
   * @return the class object that is in sync with any file system change
   *
   * @throws ClassNotFoundException
   *         if a class with the configured binary name could not be found
   * @throws IOException
   *         if an I/O error occurs
   */
  Class<?> get() throws ClassNotFoundException, IOException;

}