/**
 * GRAPHITE SOFTWARE CONFIDENTIAL
 * 
 * Copyright 2014. Graphite Software Corporation. All Rights Reserved.
 * 
 * NOTICE: All information contained herein is, and remains the property of 
 * Graphite Software Corporation and its suppliers, if any. The intellectual and 
 * technical concepts contained herein are proprietary to Graphite Software 
 * Corporation and its suppliers and may be covered by Canada, U.S. and Foreign 
 * Patents, patents in process, and are protected by trade secret or copyright 
 * law. Dissemination of this information or reproduction of this material is 
 * strictly forbidden unless prior written permission is obtained from Graphite 
 * Software Corporation.
 */
package com.securespaces.wizard.server;

public class ErrorResponse extends ServerResponse{
    public static final String SPACE_NOT_IN_REGISTERED_STATE = "Space is not in registered state, cannot be activated";
    public static final String INVITATION_KEY_NOT_FOUND = "Key not found for invitation";
    public String msg;
}
