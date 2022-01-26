ScriptName BasicScript Extends Form Hidden Conditional


{A multiline


doc comment}

Int Property Foo = 23 Auto

{another doc comments





!}

Int[] Function Test()
  return New Int[2]
EndFunction

Int Function Len()
  return Test().Length
EndFunction

Int Function Zero()
  return Test()[0]
EndFunction