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
package org.hyperledger.besu.ethereum.api.jsonrpc.internal.privacy.methods.priv;

import static org.apache.logging.log4j.LogManager.getLogger;

import org.hyperledger.besu.enclave.Enclave;
import org.hyperledger.besu.enclave.types.DeletePrivacyGroupRequest;
import org.hyperledger.besu.ethereum.api.jsonrpc.RpcMethod;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.JsonRpcRequestContext;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.methods.JsonRpcMethod;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.response.JsonRpcError;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.response.JsonRpcResponse;
import org.hyperledger.besu.ethereum.api.jsonrpc.internal.response.JsonRpcSuccessResponse;
import org.hyperledger.besu.ethereum.core.PrivacyParameters;

import org.apache.logging.log4j.Logger;

public class PrivDeletePrivacyGroup implements JsonRpcMethod {

  private static final Logger LOG = getLogger();
  private final Enclave enclave;
  private PrivacyParameters privacyParameters;

  public PrivDeletePrivacyGroup(final Enclave enclave, final PrivacyParameters privacyParameters) {
    this.enclave = enclave;
    this.privacyParameters = privacyParameters;
  }

  @Override
  public String getName() {
    return RpcMethod.PRIV_DELETE_PRIVACY_GROUP.getMethodName();
  }

  @Override
  public JsonRpcResponse response(final JsonRpcRequestContext requestContext) {
    LOG.trace("Executing {}", RpcMethod.PRIV_DELETE_PRIVACY_GROUP.getMethodName());

    final String privacyGroupId = requestContext.getRequiredParameter(0, String.class);

    LOG.trace(
        "Deleting a privacy group with privacyGroupId {} and from {}",
        privacyGroupId,
        privacyParameters.getEnclavePublicKey());

    DeletePrivacyGroupRequest deletePrivacyGroupRequest =
        new DeletePrivacyGroupRequest(privacyGroupId, privacyParameters.getEnclavePublicKey());
    String response;
    try {
      response = enclave.deletePrivacyGroup(deletePrivacyGroupRequest);
    } catch (Exception e) {
      LOG.error("Failed to fetch transaction from Enclave with error " + e.getMessage());
      LOG.error(e);
      return new JsonRpcSuccessResponse(
          requestContext.getRequest().getId(), JsonRpcError.DELETE_PRIVACY_GROUP_ERROR);
    }
    return new JsonRpcSuccessResponse(requestContext.getRequest().getId(), response);
  }
}
