package fundamentals;

import java.util.*;
import java.util.function.Predicate;

/**
 * In this exercise, you have to implement a basic filesystem for a new operating system.
 *
 * A filesystem is a tree-based data structure, where each node corresponds to
 * one directory. The root node is the entry point of the filesystem.
 * Each directory can contain multiple subdirectories, as well as multiple files.
 */

public class FileSystem
{
    /**
     * Class that represents one file on the filesystem. A
     * file is characterized by a name (e.g., "Hello.txt") and by a
     * size (expressed in bytes).
     */
    static class File {
        private final String name;
        private final int size;

        /**
         * Constructs a file with the given name and size.
         */
        public File(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }
    }


    /**
     * Class that represents one directory on the filesystem. A
     * directory is characterized by a name, by its subdirectories,
     * and by the files it stores.
     */
    static class Directory implements Iterable<File> {

        // TODO: Add the member variables you need here
        private String name;
        private List<File> files;
        private List<Directory> subDirectories;

        /**
         * Constructs a new Directory with the given name.
         * The directory is initially empty, with no files or subdirectories.
         *
         * @param name the name of the directory
         */
        public Directory(String name) {
            // TODO: constructor
            this.name = name;
            this.files = new ArrayList<>();
            this.subDirectories = new ArrayList<>();
        }

        /**
         * Returns the name of this directory.
         */
        public String getName() {
            // TODO
             return name;
        }

        /**
         * Add a new file to this directory.
         *
         * @param file the file to be added
         */
        public void addFile(File file) {
            // TODO
            files.add(file);
        }

        /**
         * Add a new subdirectory to this directory.
         *
         * @param directory the subdirectory to be added
         */
        public void addDirectory(Directory directory) {
            // TODO
            subDirectories.add(directory);
        }

        /**
         * Returns the total size of all files in this directory and its subdirectories.
         *
         * @return the total size (expressed in bytes)
         */
        public int getTotalSize() {
            // TODO
            int totalSize = 0;
            for (File file: files)
                totalSize += file.getSize();

            for (Directory dir: subDirectories)
                totalSize += dir.getTotalSize();

            return totalSize;
        }

        protected List<File> getAllFiles() {
            List<File> allFiles = new ArrayList<>(files);
            for (Directory dir : subDirectories) {
                allFiles.addAll(dir.getAllFiles());
            }
            return allFiles;
        }

        /**
         * Returns an iterator over all the files in the Directory,
         * including files in its subdirectories. The order of the files is arbitrary.
         *
         * The FileSystem is assumed not be modified while iterating over the files:
         * There is thus no need to worry about ConcurrentModificationException.
         *
         * @return the iterator over all the files
         */
        @Override
        public Iterator<File> iterator() {
            // TODO
             return new LazyFileIterator(this, f -> true) ;
        }

        /**
         * Returns an iterator over the files in the Directory that match the given filter,
         * including files in its subdirectories. The order of the files is arbitrary.
         *
         * The FileSystem is assumed not be modified while iterating over the files:
         * There is thus no need to worry about ConcurrentModificationException.
         *
         * @param filter a predicate to filter the files of interest
         * @return the iterator over the filtered files
         */
        public Iterator<File> iterator(Predicate<File> filter) {
            // TODO
             return new LazyFileIterator(this, filter);
        }
    }

    static class FileIterator implements Iterator<File> {
        Deque<File> fileStack;
        Predicate<File> filter;

        public FileIterator(Directory root, Predicate<File> filter) {
            fileStack = new ArrayDeque<>();
            this.filter = filter;
            pushAllFiles(root);
        }

        private void pushAllFiles(Directory directory) {
            List<File> files = directory.getAllFiles();
            for (File file : files) {
                if (filter == null || filter.test(file))
                    fileStack.push(file);
            }
        }

        @Override
        public boolean hasNext() {
            return !fileStack.isEmpty();
        }

        @Override
        public File next() {
            return fileStack.pop();
        }
    }

    static class LazyFileIterator implements Iterator<File> {

        private final Deque<Directory> directoryStack;
        private final Predicate<File> filter;

        private Iterator<File> currentDirectoryFiles;
        private File nextFile;

        public LazyFileIterator(Directory root, Predicate<File> filter) {
            this.directoryStack = new ArrayDeque<>();
            this.filter = filter;
            this.currentDirectoryFiles = Collections.emptyIterator();
            this.nextFile = null;

            if (root != null)
                directoryStack.push(root);
        }

        @Override
        public boolean hasNext() {
            if (nextFile != null)
                return true;
            advance();
            return nextFile != null;
        }

        @Override
        public File next() {
            if (!hasNext())
                throw new NoSuchElementException();
            File result = nextFile;
            nextFile = null;
            return result;
        }

        private void advance() {
            while (nextFile == null) {
                if (currentDirectoryFiles.hasNext()) {
                    File candidate = currentDirectoryFiles.next();
                    if (filter == null || filter.test(candidate)) {
                        nextFile = candidate;
                        return;
                    }
                    continue;
                }

                if (directoryStack.isEmpty())
                    return;

                Directory currentDir = directoryStack.pop();
                currentDirectoryFiles = currentDir.files.iterator();

                for (Directory subDir : currentDir.subDirectories) {
                    directoryStack.push(subDir);
                }
            }
        }
    }

}
