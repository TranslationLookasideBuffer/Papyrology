package(default_visibility = ["//visibility:public"])

java_library(
    name = "junit",
    testonly = True,
    exports = ["@maven//:junit_junit"],
)

java_library(
    name = "truth",
    testonly = True,
    exports = [
        "@maven//:com_google_truth_extensions_truth_java8_extension",
        "@maven//:com_google_truth_truth",
    ],
)

java_plugin(
    name = "autovalue_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    visibility = ["//visibility:private"],
    deps = ["@maven//:com_google_auto_value_auto_value"],
)

java_plugin(
    name = "autooneof_plugin",
    processor_class = "com.google.auto.value.processor.AutoOneOfProcessor",
    visibility = ["//visibility:private"],
    deps = ["@maven//:com_google_auto_value_auto_value"],
)

java_library(
    name = "autovalue",
    exported_plugins = [
        ":autovalue_plugin",
        ":autooneof_plugin",
    ],
    neverlink = True,
    exports = ["@maven//:com_google_auto_value_auto_value"],
)

java_library(
    name = "autovalue_annotations",
    exports = ["@maven//:com_google_auto_value_auto_value_annotations"],
)

java_library(
    name = "errorprone",
    exports = ["@maven//:com_google_errorprone_error_prone_annotations"],
)

java_library(
    name = "guava",
    exports = ["@maven//:com_google_guava_guava"],
)

java_library(
    name = "flogger",
    exports = ["@maven//:com_google_flogger_flogger"],
)

java_library(
    name = "antlr",
    exports = ["@antlr4_runtime//jar"],
)
