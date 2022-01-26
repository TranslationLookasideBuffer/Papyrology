java_library(
    name = "node",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/org/nullable/papyrology/ast:__pkg__",
        "//src/test/java/org/nullable/papyrology/ast:__pkg__",
    ],
    deps = [
        "//src/main/java/org/nullable/papyrology/ast/common",
        "//src/main/java/org/nullable/papyrology/grammar",
        "//src/main/third_party:antlr",
        "//src/main/third_party:autovalue",
        "//src/main/third_party:autovalue_annotations",
        "//src/main/third_party:errorprone",
        "//src/main/third_party:guava",
    ],
)
