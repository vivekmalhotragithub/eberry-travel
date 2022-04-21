package com.eberry.travel.api;

import java.util.List;

public record ApiResult(List<ApiRejectedTransaction> rejectedTransactions) {
}


