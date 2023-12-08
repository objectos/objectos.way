/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.git;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import objectos.fs.Directory;

/**
 * Support Git packfiles (iter 1). More specifically:
 *
 * - bare repository containing a single pack file (repo02)
 * - pack file v2
 * - pack index v2
 * - blob deltified (depth 1) & non deltified
 * - commit non deltified
 * - tree deltified (depth 1) & non deltified
 *
 * External links:
 *
 * - https://git-scm.com/book/en/v2/Git-Internals-Packfiles
 * - https://git-scm.com/docs/pack-format
 * - https://codewords.recurse.com/issues/three/unpacking-git-packfiles
 * - https://github.com/cirosantilli/git-tutorial/blob/master/packfiles.md
 *
 * @since 3
 */
final class TestCase12 {

  public static final String DESCRIPTION = "Support Git packfiles (iter 1)";

  private TestCase12() {}

  public static byte[] getBlobContents() {
    return TestingGit.decode64(
        "IyBPYmplY3Rvc1JlcG8KClRoaXMgaXMgYSBnaXQgcmVwb3NpdG9yeSBtZWFudCB0byBiZSB1c2Vk",
        "IGluIHRlc3RzLgo="
    );
  }

  public static ObjectId getBlobDeltified() throws InvalidObjectIdFormatException {
    return ObjectId.parse("6eaf9247b35bbc35676d1698313381be80a4bdc4");
  }

  public static byte[] getCommitContents() {
    return TestingGit.decode64(
        "dHJlZSAzM2ZjZTRlNmVmMjRkYTZhOWMzOGJmZWU1NGM1MDhhMjI4MjIwMmQxCnBhcmVudCBiOWM0",
        "ZjJkYjdiNGZkNzQyOTkwYjUxOGVlM2M4YWU1OWViMWQ2ZTkzCmF1dGhvciBUaGUgQXV0aG9yIDxh",
        "dXRob3JAZXhhbXBsZS5jb20+IDE2MTU1NTE1MjkgLTAzMDAKY29tbWl0dGVyIFRoZSBDb21taXR0",
        "ZXIgPGNvbW1pdHRlckBleGFtcGxlLmNvbT4gMTYxNTU1MTUyOSAtMDMwMAoKW2Jpbl0gYWRkIGEg",
        "Y2kgc2NyaXB0Cg=="
    );
  }

  public static ObjectId getCommitNonDeltified() throws InvalidObjectIdFormatException {
    return ObjectId.parse("8ae78c68a5eb6abf81e7bd006d7d9bf326780589");
  }

  public static Directory getDirectory() throws IOException {
    return TestingGit.repo02();
  }

  public static Directory getObjectsDirectory() throws IOException {
    Directory directory;
    directory = getDirectory();

    return directory.getDirectory("objects");
  }

  public static PackFile getPackFile() throws IOException, ExecutionException {
    Repository repository;
    repository = getRepository();

    return repository.getPackFile(0);
  }

  public static ObjectId getPackName() throws InvalidObjectIdFormatException {
    return ObjectId.parse("d5cc4c3dcc31a2c4078d70da2d3ae7cda92bd7ae");
  }

  public static Repository getRepository() throws IOException, ExecutionException {
    Directory directory;
    directory = getDirectory();

    return TestingGit.bareRepository(directory);
  }

  public static byte[] getTreeContents() {
    return TestingGit.decode64(
        "MTAwNjQ0IFJFQURNRS5tZABur5JHs1u8NWdtFpgxM4G+gKS9xDQwMDAwIGJpbgDc+uka5Ckws8eq",
        "Q4QE+5a1wlBo5w=="
    );
  }

  public static ObjectId getTreeDeltified() throws InvalidObjectIdFormatException {
    return ObjectId.parse("33fce4e6ef24da6a9c38bfee54c508a2282202d1");
  }

  public static void repositoryTo(Path root) throws IOException {
    Path path;
    path = TestingGit2.repo02();

    TestingGit2.copyRecursively(path, root);
  }

  /*

  $ git verify-pack -v objects/pack/pack-d5cc4c3dcc31a2c4078d70da2d3ae7cda92bd7ae.idx
  d24fe44733cf3768b8d32bd0eaad296f74ed0bdb commit 227 159 12
  f68a4642ee64801279104652cf93d177d3551e31 commit 241 159 171
  717271f0f0ee528c0bb094e8b2f84ea6cef7b39d commit 232 159 330
  c5dabe365df71fa3aed52d99157a6b5c59bc13af commit 232 159 489
  68699720c357a5ce1d4171a65ce801741736ea31 commit 238 164 648
  293db19c76f6645343bfdcaf9ae54d47951ddf6a commit 239 168 812
  8ae78c68a5eb6abf81e7bd006d7d9bf326780589 commit 238 166 980
  b9c4f2db7b4fd742990b518ee3c8ae59eb1d6e93 commit 182 133 1146
  ad17c7d263b5cd99293776f306bf44faa5099f0f tree   97 105 1279
  5a7c6718548599065e7731186ac7391ca6b5e957 blob   96 93 1384
  f173feaefcfdd9440b5907d2a0cdf9e0e7dd88bb tree   93 100 1477
  1584aeadeea6a620b0d91016b7aa1eca2f62af46 blob   40 50 1577
  a9bf588e2f88457fdf73ac7361ef1d596fb81453 blob   12 21 1627
  bc6a9161e7e8e8c965bb06247ad2ac64401672f5 tree   38 48 1648
  fb72221c840907a404a4433b2f3222fda77db320 blob   26 36 1696
  db1f00b4e55df768ad43dd76af4d2dbac2cea73d tree   62 69 1732
  0fc7a9f2457ab0488441ca780d590b1ec26c75ee tree   31 40 1801
  d6c9538a02df64698b51a3427e455bbf0d3e2a55 tree   29 38 1841
  09cb9b7b846756738668aae8369dffe887e2fbba tree   30 39 1879
  453088f819dcbeed8245981f47805f5c3daffeb7 tree   35 44 1918
  a985785d9534c527ace20fefa5dee6ad6f7c7f86 tree   73 77 1962
  f092def907ed3dd7aa3632063598518b1ef25433 blob   46 56 2039
  4cf27def690a4b1cf92d49a653003cbda787ca5f blob   48 58 2095
  4009d44c75039f653e2dbf022d79d97a15c24162 tree   31 40 2153
  d3906ef880ca4bdc3b3271e7efb341b51dd9898c tree   29 38 2193
  5a94ac4d34533dd1cda87ca519637dc914bcf197 tree   30 39 2231
  792fc642dcfe46e83b5122db300eaa7cddfff72e tree   35 45 2270
  a658c759a9970a9190d3a2d454afd344fc80adb9 tree   81 80 2315
  2d20bb843e1c2a37537066bc6c2da77b824c1bd2 blob   50 60 2395
  1a4808941ac8f4c7cbe812593804f40a4102b99f blob   52 62 2455
  cf9a728b066ba3762b7fb441ccf5fc50658b09cb tree   97 105 2517
  6eaf9247b35bbc35676d1698313381be80a4bdc4 blob   4 15 2622 1 5a7c6718548599065e7731186ac7391ca6b5e957
  8ef9199f8253fdb0d622f0e9aee798349d28d8f7 tree   25 37 2637 1 cf9a728b066ba3762b7fb441ccf5fc50658b09cb
  8e3578c3dd59a6739102031513732cf99d14f3c6 tree   62 70 2674
  eacce2c6525173d6a0366463739611cc9014d3c7 tree   31 40 2744
  b3b7dbc9182013995993f6358ef319823d7fad06 tree   29 38 2784
  1b738a154b5e38020d733301114f1931dbd1d48d tree   30 39 2822
  8dd0f9048878b59a4188b0647bb509e0ba138f8b tree   35 44 2861
  98f083a5fb687304c0fbc5cf81b42f8f2b6c9d0c tree   37 48 2905
  4d9bfebdfedec184c87900717de5d4e09ff9e128 tree   31 40 2953
  84937d78a8ced6240d4820ea0a38ab231cef2d25 tree   29 38 2993
  337b6c1f6e1824d0080dff43d75dd354efac803b tree   30 39 3031
  58d56cd9122105fc530b0c7ba7b4693db3d81a0d tree   35 45 3070
  fa7bdaeb40a7a8b7e7f4b3a280f5393605daf83c tree   41 52 3115
  bcaaa5a62b46019cc9a11dde75a1cbc05aea37a8 tree   97 104 3167
  1eda06f88aa783287b37817f287fbcbf318b8770 tree   61 72 3271
  4409e41cfb67698910852a5d60bc69bf04a41b93 tree   97 105 3343
  dcfae91ae42930b3c7aa438404fb96b5c25068e7 tree   30 41 3448
  debb4b3dca053a8585bd3d1bcbba542a0c42fc86 tree   31 41 3489
  33fce4e6ef24da6a9c38bfee54c508a2282202d1 tree   4 15 3530 1 4409e41cfb67698910852a5d60bc69bf04a41b93
  1cd042294d3933032f5fbb9735034dcbce689dc9 tree   37 48 3545
  non delta: 48 objects
  chain length = 1: 3 objects
  objects/pack/pack-d5cc4c3dcc31a2c4078d70da2d3ae7cda92bd7ae.pack: ok

   */

}