# Design notes

## Categorical programming

**WARNING** depending on several scope issues, if you import ambiguous modules for the same datatype (say `listFunctor` _and_ `listMonad`) you'll get either 

- `method xxx not a member of ...` which is wrong, or 
- `ambiguous implicit conversions ...` which is what you should get

Could be related: [How to avoid ambiguous implicits - scala-user](https://groups.google.com/d/msg/scala-user/g2pzbWT2Igc/Ag1X7vst8r8J). Anyway, it is certainly a bug.