package com.jetpackduba.gitnuro.ui.diff.syntax_highlighter

class JavaSyntaxHighlighter : SyntaxHighlighter() {
    override fun loadKeywords(): List<String> = listOf(
        "abstract",
        "continue",
        "for",
        "new",
        "switch",
        "assert",
        "default",
        "goto*",
        "package",
        "synchronized",
        "boolean",
        "do",
        "if",
        "private",
        "this",
        "break",
        "double",
        "implements",
        "protected",
        "throw",
        "byte",
        "else",
        "import",
        "public",
        "throws",
        "case",
        "enum****",
        "instanceof",
        "return",
        "transient",
        "catch",
        "extends",
        "int",
        "short",
        "try",
        "char",
        "final",
        "interface",
        "static",
        "void",
        "class",
        "finally",
        "long",
        "strictfp**",
        "volatile",
        "const*",
        "float",
        "native",
        "super",
        "while",
    )

    override fun isAnnotation(word: String): Boolean = word.startsWith("@")
    override fun isComment(line: String): Boolean = line.startsWith("//")
}