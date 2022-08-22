// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: BaseResponseProto.proto

package com.zzk.common.protocol;

public final class ZIMResponseProto {
  private ZIMResponseProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ZIMResProtocolOrBuilder extends
      // @@protoc_insertion_point(interface_extends:protocol.ZIMResProtocol)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 type = 1;</code>
     * @return The type.
     */
    int getType();

    /**
     * <code>string username = 2;</code>
     * @return The username.
     */
    java.lang.String getUsername();
    /**
     * <code>string username = 2;</code>
     * @return The bytes for username.
     */
    com.google.protobuf.ByteString
        getUsernameBytes();

    /**
     * <code>string resMsg = 3;</code>
     * @return The resMsg.
     */
    java.lang.String getResMsg();
    /**
     * <code>string resMsg = 3;</code>
     * @return The bytes for resMsg.
     */
    com.google.protobuf.ByteString
        getResMsgBytes();
  }
  /**
   * Protobuf type {@code protocol.ZIMResProtocol}
   */
  public static final class ZIMResProtocol extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:protocol.ZIMResProtocol)
      ZIMResProtocolOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use ZIMResProtocol.newBuilder() to construct.
    private ZIMResProtocol(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ZIMResProtocol() {
      username_ = "";
      resMsg_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new ZIMResProtocol();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ZIMResProtocol(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              type_ = input.readInt32();
              break;
            }
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();

              username_ = s;
              break;
            }
            case 26: {
              java.lang.String s = input.readStringRequireUtf8();

              resMsg_ = s;
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zzk.common.protocol.ZIMResponseProto.internal_static_protocol_ZIMResProtocol_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zzk.common.protocol.ZIMResponseProto.internal_static_protocol_ZIMResProtocol_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.class, com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.Builder.class);
    }

    public static final int TYPE_FIELD_NUMBER = 1;
    private int type_;
    /**
     * <code>int32 type = 1;</code>
     * @return The type.
     */
    @java.lang.Override
    public int getType() {
      return type_;
    }

    public static final int USERNAME_FIELD_NUMBER = 2;
    private volatile java.lang.Object username_;
    /**
     * <code>string username = 2;</code>
     * @return The username.
     */
    @java.lang.Override
    public java.lang.String getUsername() {
      java.lang.Object ref = username_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        username_ = s;
        return s;
      }
    }
    /**
     * <code>string username = 2;</code>
     * @return The bytes for username.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getUsernameBytes() {
      java.lang.Object ref = username_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        username_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int RESMSG_FIELD_NUMBER = 3;
    private volatile java.lang.Object resMsg_;
    /**
     * <code>string resMsg = 3;</code>
     * @return The resMsg.
     */
    @java.lang.Override
    public java.lang.String getResMsg() {
      java.lang.Object ref = resMsg_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        resMsg_ = s;
        return s;
      }
    }
    /**
     * <code>string resMsg = 3;</code>
     * @return The bytes for resMsg.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getResMsgBytes() {
      java.lang.Object ref = resMsg_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        resMsg_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (type_ != 0) {
        output.writeInt32(1, type_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(username_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, username_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(resMsg_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 3, resMsg_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (type_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, type_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(username_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, username_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(resMsg_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, resMsg_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol)) {
        return super.equals(obj);
      }
      com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol other = (com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol) obj;

      if (getType()
          != other.getType()) return false;
      if (!getUsername()
          .equals(other.getUsername())) return false;
      if (!getResMsg()
          .equals(other.getResMsg())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getType();
      hash = (37 * hash) + USERNAME_FIELD_NUMBER;
      hash = (53 * hash) + getUsername().hashCode();
      hash = (37 * hash) + RESMSG_FIELD_NUMBER;
      hash = (53 * hash) + getResMsg().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code protocol.ZIMResProtocol}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:protocol.ZIMResProtocol)
        com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocolOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.zzk.common.protocol.ZIMResponseProto.internal_static_protocol_ZIMResProtocol_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.zzk.common.protocol.ZIMResponseProto.internal_static_protocol_ZIMResProtocol_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.class, com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.Builder.class);
      }

      // Construct using com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        type_ = 0;

        username_ = "";

        resMsg_ = "";

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.zzk.common.protocol.ZIMResponseProto.internal_static_protocol_ZIMResProtocol_descriptor;
      }

      @java.lang.Override
      public com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol getDefaultInstanceForType() {
        return com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.getDefaultInstance();
      }

      @java.lang.Override
      public com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol build() {
        com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol buildPartial() {
        com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol result = new com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol(this);
        result.type_ = type_;
        result.username_ = username_;
        result.resMsg_ = resMsg_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol) {
          return mergeFrom((com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol other) {
        if (other == com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol.getDefaultInstance()) return this;
        if (other.getType() != 0) {
          setType(other.getType());
        }
        if (!other.getUsername().isEmpty()) {
          username_ = other.username_;
          onChanged();
        }
        if (!other.getResMsg().isEmpty()) {
          resMsg_ = other.resMsg_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int type_ ;
      /**
       * <code>int32 type = 1;</code>
       * @return The type.
       */
      @java.lang.Override
      public int getType() {
        return type_;
      }
      /**
       * <code>int32 type = 1;</code>
       * @param value The type to set.
       * @return This builder for chaining.
       */
      public Builder setType(int value) {
        
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 type = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearType() {
        
        type_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object username_ = "";
      /**
       * <code>string username = 2;</code>
       * @return The username.
       */
      public java.lang.String getUsername() {
        java.lang.Object ref = username_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          username_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string username = 2;</code>
       * @return The bytes for username.
       */
      public com.google.protobuf.ByteString
          getUsernameBytes() {
        java.lang.Object ref = username_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          username_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string username = 2;</code>
       * @param value The username to set.
       * @return This builder for chaining.
       */
      public Builder setUsername(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        username_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string username = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearUsername() {
        
        username_ = getDefaultInstance().getUsername();
        onChanged();
        return this;
      }
      /**
       * <code>string username = 2;</code>
       * @param value The bytes for username to set.
       * @return This builder for chaining.
       */
      public Builder setUsernameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        username_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object resMsg_ = "";
      /**
       * <code>string resMsg = 3;</code>
       * @return The resMsg.
       */
      public java.lang.String getResMsg() {
        java.lang.Object ref = resMsg_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          resMsg_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string resMsg = 3;</code>
       * @return The bytes for resMsg.
       */
      public com.google.protobuf.ByteString
          getResMsgBytes() {
        java.lang.Object ref = resMsg_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          resMsg_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string resMsg = 3;</code>
       * @param value The resMsg to set.
       * @return This builder for chaining.
       */
      public Builder setResMsg(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        resMsg_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string resMsg = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearResMsg() {
        
        resMsg_ = getDefaultInstance().getResMsg();
        onChanged();
        return this;
      }
      /**
       * <code>string resMsg = 3;</code>
       * @param value The bytes for resMsg to set.
       * @return This builder for chaining.
       */
      public Builder setResMsgBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        resMsg_ = value;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:protocol.ZIMResProtocol)
    }

    // @@protoc_insertion_point(class_scope:protocol.ZIMResProtocol)
    private static final com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol();
    }

    public static com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ZIMResProtocol>
        PARSER = new com.google.protobuf.AbstractParser<ZIMResProtocol>() {
      @java.lang.Override
      public ZIMResProtocol parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ZIMResProtocol(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ZIMResProtocol> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<ZIMResProtocol> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.zzk.common.protocol.ZIMResponseProto.ZIMResProtocol getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_protocol_ZIMResProtocol_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_protocol_ZIMResProtocol_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027BaseResponseProto.proto\022\010protocol\"@\n\016Z" +
      "IMResProtocol\022\014\n\004type\030\001 \001(\005\022\020\n\010username\030" +
      "\002 \001(\t\022\016\n\006resMsg\030\003 \001(\tB+\n\027com.zzk.common." +
      "protocolB\020ZIMResponseProtob\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_protocol_ZIMResProtocol_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_protocol_ZIMResProtocol_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_protocol_ZIMResProtocol_descriptor,
        new java.lang.String[] { "Type", "Username", "ResMsg", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
