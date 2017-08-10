# Curse Word Prevention

The algorithm was written with the intent of placing hashes in visible places like the URLs. It is possible that the created hash ends up accidentally contaning a bad or offensive word(s).

Therefore, it tries to avoid generating most common english curse words with the [default alphabet][javadoc-gh-pages-default-alphabet] by using separators which will never be placed next to each other.

```
c, C, f, F, h, H, i, I, s, S, t, T, u, U
```

[javadoc-gh-pages-default-alphabet]: https://arcticicestudio.github.io/icecore-hashids/javadoc/com/arcticicestudio/icecore/hashids/Hashids.html#DEFAULT_ALPHABET
