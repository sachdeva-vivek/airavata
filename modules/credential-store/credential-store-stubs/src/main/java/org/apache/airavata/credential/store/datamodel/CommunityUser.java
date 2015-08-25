    /*
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements.  See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to You under the Apache License, Version 2.0
     * (the "License"); you may not use this file except in compliance with
     * the License.  You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.airavata.credential.store.datamodel;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-8-25")
public class CommunityUser implements org.apache.thrift.TBase<CommunityUser, CommunityUser._Fields>, java.io.Serializable, Cloneable, Comparable<CommunityUser> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("CommunityUser");

  private static final org.apache.thrift.protocol.TField GATEWAY_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("gatewayName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField USERNAME_FIELD_DESC = new org.apache.thrift.protocol.TField("username", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField USER_EMAIL_FIELD_DESC = new org.apache.thrift.protocol.TField("userEmail", org.apache.thrift.protocol.TType.STRING, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new CommunityUserStandardSchemeFactory());
    schemes.put(TupleScheme.class, new CommunityUserTupleSchemeFactory());
  }

  public String gatewayName; // required
  public String username; // required
  public String userEmail; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    GATEWAY_NAME((short)1, "gatewayName"),
    USERNAME((short)2, "username"),
    USER_EMAIL((short)3, "userEmail");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // GATEWAY_NAME
          return GATEWAY_NAME;
        case 2: // USERNAME
          return USERNAME;
        case 3: // USER_EMAIL
          return USER_EMAIL;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.GATEWAY_NAME, new org.apache.thrift.meta_data.FieldMetaData("gatewayName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.USERNAME, new org.apache.thrift.meta_data.FieldMetaData("username", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.USER_EMAIL, new org.apache.thrift.meta_data.FieldMetaData("userEmail", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(CommunityUser.class, metaDataMap);
  }

  public CommunityUser() {
  }

  public CommunityUser(
    String gatewayName,
    String username,
    String userEmail)
  {
    this();
    this.gatewayName = gatewayName;
    this.username = username;
    this.userEmail = userEmail;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public CommunityUser(CommunityUser other) {
    if (other.isSetGatewayName()) {
      this.gatewayName = other.gatewayName;
    }
    if (other.isSetUsername()) {
      this.username = other.username;
    }
    if (other.isSetUserEmail()) {
      this.userEmail = other.userEmail;
    }
  }

  public CommunityUser deepCopy() {
    return new CommunityUser(this);
  }

  @Override
  public void clear() {
    this.gatewayName = null;
    this.username = null;
    this.userEmail = null;
  }

  public String getGatewayName() {
    return this.gatewayName;
  }

  public CommunityUser setGatewayName(String gatewayName) {
    this.gatewayName = gatewayName;
    return this;
  }

  public void unsetGatewayName() {
    this.gatewayName = null;
  }

  /** Returns true if field gatewayName is set (has been assigned a value) and false otherwise */
  public boolean isSetGatewayName() {
    return this.gatewayName != null;
  }

  public void setGatewayNameIsSet(boolean value) {
    if (!value) {
      this.gatewayName = null;
    }
  }

  public String getUsername() {
    return this.username;
  }

  public CommunityUser setUsername(String username) {
    this.username = username;
    return this;
  }

  public void unsetUsername() {
    this.username = null;
  }

  /** Returns true if field username is set (has been assigned a value) and false otherwise */
  public boolean isSetUsername() {
    return this.username != null;
  }

  public void setUsernameIsSet(boolean value) {
    if (!value) {
      this.username = null;
    }
  }

  public String getUserEmail() {
    return this.userEmail;
  }

  public CommunityUser setUserEmail(String userEmail) {
    this.userEmail = userEmail;
    return this;
  }

  public void unsetUserEmail() {
    this.userEmail = null;
  }

  /** Returns true if field userEmail is set (has been assigned a value) and false otherwise */
  public boolean isSetUserEmail() {
    return this.userEmail != null;
  }

  public void setUserEmailIsSet(boolean value) {
    if (!value) {
      this.userEmail = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case GATEWAY_NAME:
      if (value == null) {
        unsetGatewayName();
      } else {
        setGatewayName((String)value);
      }
      break;

    case USERNAME:
      if (value == null) {
        unsetUsername();
      } else {
        setUsername((String)value);
      }
      break;

    case USER_EMAIL:
      if (value == null) {
        unsetUserEmail();
      } else {
        setUserEmail((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case GATEWAY_NAME:
      return getGatewayName();

    case USERNAME:
      return getUsername();

    case USER_EMAIL:
      return getUserEmail();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case GATEWAY_NAME:
      return isSetGatewayName();
    case USERNAME:
      return isSetUsername();
    case USER_EMAIL:
      return isSetUserEmail();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof CommunityUser)
      return this.equals((CommunityUser)that);
    return false;
  }

  public boolean equals(CommunityUser that) {
    if (that == null)
      return false;

    boolean this_present_gatewayName = true && this.isSetGatewayName();
    boolean that_present_gatewayName = true && that.isSetGatewayName();
    if (this_present_gatewayName || that_present_gatewayName) {
      if (!(this_present_gatewayName && that_present_gatewayName))
        return false;
      if (!this.gatewayName.equals(that.gatewayName))
        return false;
    }

    boolean this_present_username = true && this.isSetUsername();
    boolean that_present_username = true && that.isSetUsername();
    if (this_present_username || that_present_username) {
      if (!(this_present_username && that_present_username))
        return false;
      if (!this.username.equals(that.username))
        return false;
    }

    boolean this_present_userEmail = true && this.isSetUserEmail();
    boolean that_present_userEmail = true && that.isSetUserEmail();
    if (this_present_userEmail || that_present_userEmail) {
      if (!(this_present_userEmail && that_present_userEmail))
        return false;
      if (!this.userEmail.equals(that.userEmail))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_gatewayName = true && (isSetGatewayName());
    list.add(present_gatewayName);
    if (present_gatewayName)
      list.add(gatewayName);

    boolean present_username = true && (isSetUsername());
    list.add(present_username);
    if (present_username)
      list.add(username);

    boolean present_userEmail = true && (isSetUserEmail());
    list.add(present_userEmail);
    if (present_userEmail)
      list.add(userEmail);

    return list.hashCode();
  }

  @Override
  public int compareTo(CommunityUser other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetGatewayName()).compareTo(other.isSetGatewayName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGatewayName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gatewayName, other.gatewayName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUsername()).compareTo(other.isSetUsername());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUsername()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.username, other.username);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUserEmail()).compareTo(other.isSetUserEmail());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserEmail()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userEmail, other.userEmail);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("CommunityUser(");
    boolean first = true;

    sb.append("gatewayName:");
    if (this.gatewayName == null) {
      sb.append("null");
    } else {
      sb.append(this.gatewayName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("username:");
    if (this.username == null) {
      sb.append("null");
    } else {
      sb.append(this.username);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("userEmail:");
    if (this.userEmail == null) {
      sb.append("null");
    } else {
      sb.append(this.userEmail);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (gatewayName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'gatewayName' was not present! Struct: " + toString());
    }
    if (username == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'username' was not present! Struct: " + toString());
    }
    if (userEmail == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'userEmail' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class CommunityUserStandardSchemeFactory implements SchemeFactory {
    public CommunityUserStandardScheme getScheme() {
      return new CommunityUserStandardScheme();
    }
  }

  private static class CommunityUserStandardScheme extends StandardScheme<CommunityUser> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, CommunityUser struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // GATEWAY_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gatewayName = iprot.readString();
              struct.setGatewayNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // USERNAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.username = iprot.readString();
              struct.setUsernameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // USER_EMAIL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userEmail = iprot.readString();
              struct.setUserEmailIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, CommunityUser struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.gatewayName != null) {
        oprot.writeFieldBegin(GATEWAY_NAME_FIELD_DESC);
        oprot.writeString(struct.gatewayName);
        oprot.writeFieldEnd();
      }
      if (struct.username != null) {
        oprot.writeFieldBegin(USERNAME_FIELD_DESC);
        oprot.writeString(struct.username);
        oprot.writeFieldEnd();
      }
      if (struct.userEmail != null) {
        oprot.writeFieldBegin(USER_EMAIL_FIELD_DESC);
        oprot.writeString(struct.userEmail);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class CommunityUserTupleSchemeFactory implements SchemeFactory {
    public CommunityUserTupleScheme getScheme() {
      return new CommunityUserTupleScheme();
    }
  }

  private static class CommunityUserTupleScheme extends TupleScheme<CommunityUser> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, CommunityUser struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.gatewayName);
      oprot.writeString(struct.username);
      oprot.writeString(struct.userEmail);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, CommunityUser struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.gatewayName = iprot.readString();
      struct.setGatewayNameIsSet(true);
      struct.username = iprot.readString();
      struct.setUsernameIsSet(true);
      struct.userEmail = iprot.readString();
      struct.setUserEmailIsSet(true);
    }
  }

}

