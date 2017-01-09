package MessagingProvidersBenchmark

import MessagingProvidersBenchmark.buildTypes.*
import MessagingProvidersBenchmark.vcsRoots.*
import jetbrains.buildServer.configs.kotlin.v10.*
import jetbrains.buildServer.configs.kotlin.v10.Project

object Project : Project({
    uuid = "5b67dac5-e999-424e-acfc-5d3b14afd797"
    extId = "MessagingProvidersBenchmark"
    parentId = "_Root"
    name = "Messaging Providers Benchmark - Teamcity project"

    vcsRoot(MessagingProvidersBenchmark_HttpsGithubComCbirajdarMessagingProvidersBenchmarkRe)

    buildType(MessagingProvidersBenchmark_Build)
})
