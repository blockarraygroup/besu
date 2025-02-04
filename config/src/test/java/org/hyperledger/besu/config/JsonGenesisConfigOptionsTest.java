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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Resources;
import org.junit.Test;

public class JsonGenesisConfigOptionsTest {

  private ObjectNode loadCompleteDataSet() {
    try {
      final String configText =
          Resources.toString(
              Resources.getResource("valid_config_with_custom_forks.json"), StandardCharsets.UTF_8);
      return JsonUtil.objectNodeFromString(configText);
    } catch (final IOException e) {
      throw new RuntimeException("Failed to load resource", e);
    }
  }

  private ObjectNode loadConfigWithNoTransitions() {
    final ObjectNode configNode = loadCompleteDataSet();
    configNode.remove("transitions");
    return configNode;
  }

  private ObjectNode loadConfigWithNoIbft2Forks() {
    final ObjectNode configNode = loadCompleteDataSet();
    final ObjectNode transitionsNode = JsonUtil.getObjectNode(configNode, "transitions").get();
    transitionsNode.remove("ibft2");

    return configNode;
  }

  private ObjectNode loadConfigWithAnIbft2ForkWithMissingValidators() {
    final ObjectNode configNode = loadCompleteDataSet();
    final ObjectNode transitionsNode = JsonUtil.getObjectNode(configNode, "transitions").get();
    final ArrayNode ibftNode = JsonUtil.getArrayNode(transitionsNode, "ibft2").get();
    ((ObjectNode) ibftNode.get(0)).remove("validators");

    return configNode;
  }

  @Test
  public void transitionsDecodesCorrectlyFromFile() {
    final ObjectNode configNode = loadCompleteDataSet();

    final JsonGenesisConfigOptions configOptions =
        JsonGenesisConfigOptions.fromJsonObject(configNode);

    assertThat(configOptions.getTransitions()).isNotNull();
    assertThat(configOptions.getTransitions().getIbftForks().size()).isEqualTo(2);
    assertThat(configOptions.getTransitions().getIbftForks().get(0).getForkBlock()).isEqualTo(20);
    assertThat(configOptions.getTransitions().getIbftForks().get(0).getValidators()).isNotEmpty();
    assertThat(configOptions.getTransitions().getIbftForks().get(0).getValidators().get())
        .containsExactly(
            "0x12345678901234567890123456789012345678900x1234567890123456789012345678901234567890",
            "0x98765432109876543210987654321098765432100x9876543210987654321098765432109876543210");

    assertThat(configOptions.getTransitions().getIbftForks().get(1).getForkBlock()).isEqualTo(25);
    assertThat(configOptions.getTransitions().getIbftForks().get(1).getValidators()).isNotEmpty();
    assertThat(configOptions.getTransitions().getIbftForks().get(1).getValidators().get())
        .containsExactly(
            "0x12345678901234567890123456789012345678900x1234567890123456789012345678901234567890");
  }

  @Test
  public void configWithMissingTransitionsIsValid() {
    final ObjectNode configNode = loadConfigWithNoTransitions();

    final JsonGenesisConfigOptions configOptions =
        JsonGenesisConfigOptions.fromJsonObject(configNode);

    assertThat(configOptions.getTransitions()).isNotNull();
    assertThat(configOptions.getTransitions().getIbftForks().size()).isZero();
  }

  @Test
  public void configWithNoIbft2ForksIsValid() {
    final ObjectNode configNode = loadConfigWithNoIbft2Forks();

    final JsonGenesisConfigOptions configOptions =
        JsonGenesisConfigOptions.fromJsonObject(configNode);

    assertThat(configOptions.getTransitions()).isNotNull();
    assertThat(configOptions.getTransitions().getIbftForks().size()).isZero();
  }

  @Test
  public void configWithAnIbftWithNoValidatorsListedIsValid() {
    final ObjectNode configNode = loadConfigWithAnIbft2ForkWithMissingValidators();

    final JsonGenesisConfigOptions configOptions =
        JsonGenesisConfigOptions.fromJsonObject(configNode);

    assertThat(configOptions.getTransitions().getIbftForks().get(0).getValidators().isPresent())
        .isFalse();
    assertThat(configOptions.getTransitions().getIbftForks().get(1).getValidators().get().size())
        .isEqualTo(1);
  }
}
