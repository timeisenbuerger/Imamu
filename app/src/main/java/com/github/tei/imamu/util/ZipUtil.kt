package com.github.tei.imamu.util

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipUtil
{
    companion object
    {
        private const val BUFFER_SIZE = 6 * 1024

        fun zip(files: List<File>, zipFileName: String?) : File
        {
            var origin: BufferedInputStream?
            val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFileName)))
            try
            {
                val data = ByteArray(BUFFER_SIZE)
                for (i in files.indices)
                {
                    val fi = FileInputStream(files[i])
                    origin = BufferedInputStream(fi, BUFFER_SIZE)
                    try
                    {
                        val entry = ZipEntry(files[i].absolutePath.substring(files[i].absolutePath.lastIndexOf("/") + 1))
                        out.putNextEntry(entry)
                        var count = 0
                        while (origin.read(data, 0, BUFFER_SIZE)
                                .also { count = it } != -1
                        )
                        {
                            out.write(data, 0, count)
                        }
                    }
                    finally
                    {
                        origin.close()
                    }
                }
            }
            finally
            {
                out.close()
            }

            return File(zipFileName)
        }
    }
}