# Java _Locale_ class impact on localized strings. Examples and statistics.

## Purpose of this project
The purpose is to produce various stats around Java Locale usage. And formatting differences and similarities.

## Previous goal
One of the previous goals was to produce static pages with structered info and examples for every locale.
However, after a discovery this great website [https://www.localeplanet.com/java/sv-SE/index.html](https://www.localeplanet.com/java/sv-SE/index.html),
 a pivot was made: to produce some statistics (just for fun of it).

## Metric #1. Distribution of short DATE formats.
**Date YYYY-MM-DD [2025-12-31].**
Below is a number of locales using various date formats, sorted by number of locales descending.<br/>
In other words: the most _popular_ format is on top.
```
[31/12/2025]    -> #[210] locales (US-inspired one)
[31/12/25]      -> #[97] locales
[2025-12-31]    -> #[39] locales (e.g. Sweden)
[31.12.25]      -> #[30] locales
[31‏/12‏/2025]    -> #[27] locales #format with RTL symbol TODO
[31.12.2025]    -> #[24] locales
[12/31/25]      -> #[21] locales
[31-12-2025]    -> #[10] locales
[2025/12/31]    -> #[8] locales
[31-12-25]      -> #[6] locales
[25/12/31]      -> #[5] locales
[31.12.25.]     -> #[4] locales
[25. 12. 31.]   -> #[2] locales
[31. 12. 2025]  -> #[2] locales
[31. 12. 25.]   -> #[1] locales
[2025.12.31]    -> #[1] locales
[31.12.25 г.]   -> #[1] locales
[31. 12. 2025.] -> #[1] locales
[31. 12. 25]    -> #[1] locales
[2025. 12. 31.] -> #[1] locales
[31/12 2025]    -> #[1] locales
```
