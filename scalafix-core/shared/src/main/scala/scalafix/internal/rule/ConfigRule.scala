package scalafix.internal.rule

import scalafix._
import scalafix.internal.config._
import scalafix.rule.Rule
import metaconfig.ConfError
import metaconfig.Configured

object ConfigRule {
  def apply(
      patches: ConfigRulePatches,
      getSemanticCtx: LazySemanticCtx): Configured[Option[Rule]] = {
    val configurationPatches = patches.all
    if (configurationPatches.isEmpty) Configured.Ok(None)
    else {
      getSemanticCtx(RuleKind.Semantic) match {
        case None =>
          ConfError
            .msg(".scalafix.conf patches require the Semantic API.")
            .notOk
        case Some(sctx) =>
          val rule = Rule.constant(
            ".scalafix.conf",
            configurationPatches.asPatch,
            sctx
          )
          Configured.Ok(Some(rule))
      }
    }
  }

}