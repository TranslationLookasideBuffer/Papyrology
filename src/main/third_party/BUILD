package(default_visibility = ["//visibility:public"])

java_library(
    name = "junit",
    testonly = True,
    exports = [
        "@maven//:junit_junit",
    ],
)

java_library(
    name = "truth",
    testonly = True,
    exports = [
        "@maven//:com_google_truth_extensions_truth_java8_extension",
        "@maven//:com_google_truth_truth",
    ],
)

java_library(
    name = "guava",
    exports = [
        "@maven//:com_google_guava_guava",
    ],
)

java_library(
    name = "antlr",
    exports = [
        "@antlr4_runtime//jar",
    ],
)
