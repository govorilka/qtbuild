package ru.ultrabot

import org.gradle.platform.base.BinaryContainer
import org.gradle.model.Defaults
import org.gradle.model.Path
import org.gradle.nativeplatform.NativeExecutableSpec
import org.gradle.model.RuleSource
import org.gradle.model.ModelMap
import org.gradle.platform.base.BinarySpec
import org.gradle.platform.base.ComponentSpecContainer
import org.gradle.language.cpp.CppSourceSet
import org.gradle.language.cpp.tasks.CppCompile

import com.google.common.io.Files

class QtBuild extends RuleSource {

    @Defaults
    void createQtMocTask(ComponentSpecContainer binaries, final @Path("buildDir") File buildDir) {
        binaries.beforeEach { spec ->
            spec.binaries.beforeEach { binary ->
                binary.inputs.withType(CppSourceSet.class) { sourceSet ->
                    binary.tasks.withType(CppCompile.class) { compileTask ->

                        // Вызов moc.exe для каждого заголовочного файла, который
                        // содержит макрос Q_OBJECT 
                        def destinationDir = new File(buildDir, "generated/${binary.name}/moc")
                        sourceSet.exportedHeaders.filter { x -> x.isFile() }.each {
                            if (Files.getFileExtension(it.path) == "h" && it.text.contains('Q_OBJECT')) {
                                def mocFile = new File(destinationDir, "moc_${Files.getNameWithoutExtension(it.name)}.cpp")
                                def process = "c:/Qt/5.6/msvc2013_64/bin/moc.exe -o ${mocFile} ${it.path}".execute()
                                process.waitFor()
                                if (process.exitValue()) {
                                     println process.err.text
                                }
                                else {
                                    compileTask.source(mocFile)
                                }
                            }
                        }
                    }
              }
            }
        }
    }
}
