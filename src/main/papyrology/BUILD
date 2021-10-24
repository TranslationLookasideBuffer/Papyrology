load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "grammar",
    srcs = ["Papyrus.g4"],
    package = "org.nullable.papyrology",
)

java_library(
    name = "papyrus",
    srcs = [":grammar"],
    visibility = ["//visibility:public"],
    deps = ["//src/main/third_party:antlr"],
)
