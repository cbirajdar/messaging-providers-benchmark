package MessagingProvidersBenchmark.buildTypes

import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.GradleBuildStep
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.GradleBuildStep.*
import jetbrains.buildServer.configs.kotlin.v10.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v10.triggers.VcsTrigger.*
import jetbrains.buildServer.configs.kotlin.v10.triggers.vcs

object MessagingProvidersBenchmark_Build : BuildType({
    uuid = "a0d01e88-5280-4096-9434-5c52c5edc56c"
    extId = "MessagingProvidersBenchmark_Build"
    name = "Build"

    vcs {
        root(MessagingProvidersBenchmark.vcsRoots.MessagingProvidersBenchmark_HttpsGithubComCbirajdarMessagingProvidersBenchmarkRe)

    }

    steps {
        gradle {
            tasks = "clean build"
            useGradleWrapper = true
            gradleWrapperPath = ""
        }
    }

    triggers {
        vcs {
        }
    }
})
