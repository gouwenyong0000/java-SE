package io.netty.example.study.common.auth;

import io.netty.example.study.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

}
