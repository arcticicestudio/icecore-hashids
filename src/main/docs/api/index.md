# IceCore Hashids API

The IceCore Hashids API provides the methods to [encode numbers][guide-encoding] and [decode hashes][guide-decoding].

You can get started by reading the chapters about [imports][imports] and the [configuration of instances][guide-config-instances], their [opt-in features][instances-features] and [how to enable them][guide-config-features].

## Versions

This documentation is based on the IceCore Hashids version {{book.api.version}}.

You can get the public API version from the static method `getVersion()`.

```java
Hashids.getVersion();
```

The interoperability version can be obtained via the static method `getInteropVersion()`.

```java
Hashids.getInteropVersion();
```

[guide-config-features]: guide/configuration/features.md
[guide-config-instances]: guide/configuration/index.md
[guide-decoding]: guide/decoding.md
[guide-encoding]: guide/encoding.md
[imports]: imports.md
[instances-features]: instances.md#features
