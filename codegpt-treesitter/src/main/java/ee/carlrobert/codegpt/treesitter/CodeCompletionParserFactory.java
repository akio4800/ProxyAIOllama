package ee.carlrobert.codegpt.treesitter;

import org.treesitter.*;

public class CodeCompletionParserFactory {

  public static CodeCompletionParser getParserForFileExtension(String extension) {
    var language = getLanguageForExtension(extension);
    if (language == null) {
      return null;
    }

    return new CodeCompletionParser(language);
  }

  private static TSLanguage getLanguageForExtension(String extension) {
    return switch (extension) {
      case "java" -> new TreeSitterJava();
      case "php" -> new TreeSitterPhp();
      case "py" -> new TreeSitterPython();
      case "ts", "tsx" -> new TreeSitterTypescript();
      case "js", "jsx" -> new TreeSitterJavascript();
      case "c", "h", "cpp", "cxx", "cc", "c++", "hpp", "hxx", "hh", "h++" -> new TreeSitterCpp();
      case "cs" -> new TreeSitterCSharp();
      case "css" -> new TreeSitterCss();
      case "dart" -> new TreeSitterDart();
      case "dockerfile" -> new TreeSitterDockerfile();
      case "elixir", "ex", "exs" -> new TreeSitterElixir();
      case "erl", "hrl" -> new TreeSitterErlang();
      case "f90", "f95", "f03", "f08" -> new TreeSitterFortran();
      case "gitattributes" -> new TreeSitterGitattributes();
      case "go" -> new TreeSitterGo();
      case "graphql", "gql" -> new TreeSitterGraphql();
      case "html", "htm" -> new TreeSitterHtml();
      case "json" -> new TreeSitterJson();
      case "kotlin", "kt", "kts" -> new TreeSitterKotlin();
      case "latex", "tex" -> new TreeSitterLatex();
      case "lua" -> new TreeSitterLua();
      case "m68k" -> new TreeSitterM68k();
      case "markdown", "md" -> new TreeSitterMarkdown();
      case "objc", "m", "mm" -> new TreeSitterObjc();
      case "perl", "pl", "pm" -> new TreeSitterPerl();
      case "ruby", "rb" -> new TreeSitterRuby();
      case "rust", "rs" -> new TreeSitterRust();
      case "scala", "sc" -> new TreeSitterScala();
      case "scss" -> new TreeSitterScss();
      case "svelte" -> new TreeSitterSvelte();
      case "swift" -> new TreeSitterSwift();
      case "yml", "yaml" -> new TreeSitterYaml();
      default -> null;
    };
  }
}
