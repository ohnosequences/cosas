# Towards cosas 1.0

- *cosas* will be a library giving you basic tools for universe-generic programming in Scala
- The basic constructs are types and their denotations
- Dependent functions and bounded products are here to stay. We will work on improving their APIs.

## Dependent functions

## Bounded products

## Universes

Basic constructs on types

- product types
- function types
- records (product types without duplicates)

We will consider coproducts in the future. There is no need for properties.

### How to use it?

Scarph is the canonical example. Use a type for defining your universe.

# Features per datatype

## bounded products

- [x] map
- [x] projections: by position and first of type
- [x] toList
- [x] drop and take
- [ ] splitAt
- [ ] any, all for predicates
- [x] filter
- [ ] findFirst

## Dependent functions

- [x] composition
- [x] interop with std functions
- [ ] universal property for bounded products

## product types

- [x] select by type and position

## records

- [x] get by type
- [ ] reorder?
- [ ] update?
- [ ] map?
