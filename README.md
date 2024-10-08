# Easy Mail

[![Latest release](https://img.shields.io/github/release/thunderbird/thunderbird-android.svg?style=flat-square)](https://github.com/thunderbird/thunderbird-android/releases/latest)
[![Latest beta release](https://img.shields.io/github/v/release/thunderbird/thunderbird-android.svg?include_prereleases&style=flat-square)](https://github.com/thunderbird/thunderbird-android/releases)

Easy Mail is an open-source email client for Android.

## Download

Easy Mail can be downloaded from a couple of sources:

- [Google Play](https://play.google.com/store/apps/details?id=com.fsck.k9)
- [F-Droid](https://f-droid.org/repository/browse/?fdid=com.fsck.k9)
- [Github Releases](https://github.com/thunderbird/thunderbird-android/releases)

You might also be interested in becoming a [tester](https://forum.k9mail.app/t/how-do-i-become-a-beta-tester/68) to get an early look at new versions.

## Release Notes

Check out the [Release Notes](https://github.com/thunderbird/thunderbird-android/wiki/ReleaseNotes) to find out what changed
in each version of Easy Mail. For a more up to date list of changes, see the XML [Changelog](app/ui/legacy/src/main/res/raw/changelog_master.xml).

## Need Help?

If the app is not behaving like it should, you might find these resources helpful:

- [User Manual](https://docs.k9mail.app/)
- [Frequently Asked Questions](https://forum.k9mail.app/c/faq)
- [Support Forum](https://forum.k9mail.app/)

## Translations

Interested in helping to translate Easy Mail? Contribute here:

- [Easy Mail localization](https://hosted.weblate.org/projects/tb-android/)

## Contributing

We welcome contributions from everyone. Please see the [CONTRIBUTING](docs/CONTRIBUTING.md) guide for more information.

### Forking

If you want to use a fork of this project please ensure that you replace the OAuth client setup in the `app-k9mail/src/{debug,release}/kotlin/app/k9mail/auth/K9OAuthConfigurationFactory.kt` and `app-thunderbird/src/{debug,daily,beta,release}/kotlin/net/thunderbird/android/auth/TbOAuthConfigurationFactory.kt` with your own OAuth client setup and ensure that the `redirectUri` is different to the one used in the main project. This is to prevent conflicts with the main app when both are installed on the same device.

### Architecture Decision Records (ADR)

We use [Architecture Decision Records](https://adr.github.io/) to document the architectural decisions made in the
development of Easy Mail. You can find them in the [`docs/architecture/adr`](docs/architecture/adr) directory.

For more information about our ADRs, please see the [ADRs README](docs/architecture/adr/README.md).

We encourage team members and contributors to read through our ADRs to understand the architectural decisions that
have shaped this project so far. Feel free to propose new ADRs or suggest modifications to existing ones as needed.

## Communication

Aside from discussing changes in [pull requests](https://github.com/thunderbird/thunderbird-android/pulls) and
[issues](https://github.com/thunderbird/thunderbird-android/issues) we use the following communication services:

- Matrix: [#tb-android:mozilla.org](https://matrix.to/#/#tb-android:mozilla.org)

## Security

The code in this repository was undergoing an extensive security audit in collaboration with the Open Source Technology
Improvement Fund ([OSTIF](https://ostif.org/)) and [7ASecurity](https://7asecurity.com/) in the first half of 2023. For
more details, see
our [blog post](https://blog.thunderbird.net/2023/07/Easy-mail-collaborates-with-ostif-and-7asecurity-security-audit/).

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
