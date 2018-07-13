package  com.carl.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension

class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        println("========================")
        println("hello gradle plugin!")
        println("========================")
        project.extensions.create("carl",MyExtention,project)
        def android = project.extensions.getByType(AppExtension)
        MyTransform myTransform = new MyTransform()
        android.registerTransform(myTransform)
    }
}