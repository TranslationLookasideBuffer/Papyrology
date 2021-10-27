load("@rules_antlr//antlr:antlr4.bzl", "antlr")
load("@io_bazel_rules_go//go:def.bzl", "go_library", "go_test")

antlr(
    name = "grammar",
    srcs = ["Papyrus.g4"],
    language = "Go"
)

go_library(
    name = "papyrus",
    srcs = [":grammar"],
    importpath = "papyrus/parser",
    visibility = ["//visibility:public"],
    deps = ["@antlr4_runtimes//:go"],
)

go_test(
    name = "papyrus_test",
    srcs = ["papyrus_test.go"],
    deps = [
        ":papyrus",
        "@antlr4_runtimes//:go",
    ],
    data = [ "//test/scripts"],
)
