/*
 * Copyright ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.besu.config;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public interface GenesisConfigOptions {

  boolean isEthHash();

  boolean isIbftLegacy();

  boolean isIbft2();

  boolean isClique();

  String getConsensusEngine();

  IbftConfigOptions getIbftLegacyConfigOptions();

  CliqueConfigOptions getCliqueConfigOptions();

  IbftConfigOptions getIbft2ConfigOptions();

  EthashConfigOptions getEthashConfigOptions();

  OptionalLong getHomesteadBlockNumber();

  OptionalLong getDaoForkBlock();

  OptionalLong getTangerineWhistleBlockNumber();

  OptionalLong getSpuriousDragonBlockNumber();

  OptionalLong getByzantiumBlockNumber();

  OptionalLong getConstantinopleBlockNumber();

  OptionalLong getConstantinopleFixBlockNumber();

  OptionalLong getIstanbulBlockNumber();

  /**
   * Block number for the Dao Fork, this value is used to tell node to connect with peer that did
   * NOT accept the Dao Fork and instead continued as what is now called the classic network
   *
   * @return block number to activate the classic fork block
   */
  OptionalLong getClassicForkBlock();

  /**
   * Block number for ECIP-1015 fork on Classic network ECIP-1015: Long-term gas cost changes for
   * IO-heavy operations to mitigate transaction spam attacks In reference to EIP-150 (ETH Tangerine
   * Whistle) Note, this fork happens after Homestead (Mainnet definition) and before DieHard fork
   *
   * @see <a
   *     href="https://ecips.ethereumclassic.org/ECIPs/ecip-1015">https://ecips.ethereumclassic.org/ECIPs/ecip-1015</a>
   * @return block number to activate ECIP-1015 code
   */
  OptionalLong getEcip1015BlockNumber();

  /**
   * Block number for DieHard fork on Classic network The DieHard fork includes changes to meet
   * specification for ECIP-1010 and EIP-160 Note, this fork happens after ECIP-1015 (classic
   * tangerine whistle) and before Gotham fork ECIP-1010: Delay Difficulty Bomb Explosion
   *
   * @see <a
   *     href="https://ecips.ethereumclassic.org/ECIPs/ecip-1010">https://ecips.ethereumclassic.org/ECIPs/ecip-1010</a>
   *     EIP-160: EXP cost increase
   * @see <a
   *     href="https://eips.ethereum.org/EIPS/eip-160">https://eips.ethereum.org/EIPS/eip-160</a>
   * @return block number to activate Classic DieHard fork
   */
  OptionalLong getDieHardBlockNumber();

  /**
   * Block number for Gotham fork on Classic network, the Gotham form includes changes to meet
   * specification for ECIP-1017 and ECIP-1039 both regarding Monetary Policy (rewards).
   *
   * @see <a
   *     href="https://ecips.ethereumclassic.org/ECIPs/ecip-1017">https://ecips.ethereumclassic.org/ECIPs/ecip-1017</a>
   *     ECIP-1017: Monetary Policy and Final Modification to the Ethereum Classic Emission Schedule
   * @see <a
   *     href="https://ecips.ethereumclassic.org/ECIPs/ecip-1039">https://ecips.ethereumclassic.org/ECIPs/ecip-1039</a>
   *     ECIP-1039: Monetary policy rounding specification
   * @return block to activate Classic Gotham fork
   */
  OptionalLong getGothamBlockNumber();

  /**
   * Block number to remove difficulty bomb, to meet specification for ECIP-1041.
   *
   * @see <a
   *     href="https://ecips.ethereumclassic.org/ECIPs/ecip-1041">https://ecips.ethereumclassic.org/ECIPs/ecip-1041</a>
   *     ECIP-1041: Remove Difficulty Bomb
   * @return block number to remove difficulty bomb on classic network
   */
  OptionalLong getDefuseDifficultyBombBlockNumber();

  /**
   * Block number for Atlantis fork on Classic network Note, this fork happen after Defuse
   * Difficulty Bomb fork and before Agharta fork ECIP-1054: Atlantis EVM and Protocol Upgrades
   * Enable the outstanding Ethereum Foundation Spurious Dragon and Byzantium network protocol
   * upgrades for the Ethereum Classic network.
   *
   * @see <a
   *     href="https://ecips.ethereumclassic.org/ECIPs/ecip-1054">https://ecips.ethereumclassic.org/ECIPs/ecip-1054</a>
   * @return block number for Atlantis fork on Classic network
   */
  OptionalLong getAtlantisBlockNumber();

  Optional<BigInteger> getChainId();

  OptionalInt getContractSizeLimit();

  OptionalInt getEvmStackSize();

  Map<String, Object> asMap();

  TransitionsConfigOptions getTransitions();
}
