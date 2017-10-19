* #125: Reverted explicit `Union` type member binding in `AnyKList`.  
    This change was introduced in #122 and was supposed not to affect anything.
    It didn't break tests (with the latest Scala versions), but it introduced
    problems in a dependent library. So this release reverts the chanege to
    avoid regression.
