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

java_library(
    name = "java_format",
    exports = [
        "@maven//:com_google_googlejavaformat_google_java_format",
    ],
)

java_binary(
    name = "java_formatter",
    main_class = "com.google.googlejavaformat.java.Main",
    runtime_deps = [":java_format"],
)

java_binary(
    name = "checkstyle",
    args = ["-c=google_checks.xml"],
    main_class = "com.puppycrawl.tools.checkstyle.Main",
    runtime_deps = ["@maven//:com_puppycrawl_tools_checkstyle"],
)
