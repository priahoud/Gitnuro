package com.jetpackduba.gitnuro.ui.diff.syntax_highlighter

class PhpSyntaxHighlighter : SyntaxHighlighter() {
    override fun loadKeywords(): List<String> = listOf(
        "__halt_compiler()",
        "abstract",
        "and",
        "array()",
        "as",
        "break",
        "callable",
        "case",
        "catch",
        "class",
        "clone",
        "const",
        "continue",
        "declare",
        "default",
        "die()",
        "do",
        "echo",
        "else",
        "elseif",
        "empty()",
        "enddeclare",
        "endfor",
        "endforeach",
        "endif",
        "endswitch",
        "endwhile",
        "eval()",
        "exit()",
        "extends",
        "final",
        "finally",
        "fn (as of PHP 7.4)",
        "for",
        "foreach",
        "function",
        "global",
        "goto",
        "if",
        "implements",
        "include",
        "include_once",
        "instanceof",
        "insteadof",
        "interface",
        "isset()",
        "list()",
        "match (as of PHP 8.0)",
        "namespace",
        "new",
        "or",
        "print",
        "private",
        "protected",
        "public",
        "readonly (as of PHP 8.1.0) *",
        "require",
        "require_once",
        "return",
        "static",
        "switch",
        "throw",
        "trait",
        "try",
        "unset()",
        "use",
        "var",
        "while",
        "xor",
        "yield",
    )

    override fun isAnnotation(word: String): Boolean = word.startsWith("@@")
    override fun isComment(line: String): Boolean = line.startsWith("//")
}