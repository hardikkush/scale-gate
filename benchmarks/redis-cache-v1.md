# ScaleGate — Redis Cache Benchmark v1

**Date:** 19 November 2026  
**Test type:** Redis warm-cache benchmark  
**Environment:** Local development machine

## 1. Objective

Evaluate the performance improvement achieved by introducing Redis caching compared with the initial database-backed baseline.

The same API endpoint and k6 workload were used for both tests.

## 2. Test Configuration

| Parameter | Value |
|---|---|
| Endpoint | `GET /api/v1/vehicles/3` |
| Virtual users | 5 |
| Test duration | 30 seconds |
| Cache configuration | Redis enabled |
| Cache manager | RedisCacheManager |
| Cache name | `vehicles` |
| Cache key | `vehicles::3` |
| Cache TTL | 1 minute |
| Load testing tool | Grafana k6 |
| Database | PostgreSQL |

**Cache preparation:** The endpoint was called through Postman before the benchmark to populate the Redis cache. The benchmark then repeatedly requested the same vehicle ID.

## 3. Benchmark Results

| Metric | Result |
|---|---:|
| Total HTTP requests | 118,147 |
| Throughput | 3,938.10 requests/sec |
| Average response time | 1.15 ms |
| Median (p50) | 1.01 ms |
| p90 response time | 1.67 ms |
| p95 response time | 2.34 ms |
| Maximum response time | 19.32 ms |
| HTTP request failure rate | 0% |
| Check success rate | 100% |

## 4. Comparison Against Database-Backed Baseline

| Metric | No Cache | Redis Cache |
|---|---:|---:|
| Throughput | 1,358.92 RPS | 3,938.10 RPS |
| Average latency | 3.37 ms | 1.15 ms |
| p50 | 1.99 ms | 1.01 ms |
| p90 | 7.10 ms | 1.67 ms |
| p95 | 9.61 ms | 2.34 ms |
| Maximum latency | 77.82 ms | 19.32 ms |
| HTTP failures | 0% | 0% |

## 5. Performance Improvements

- Throughput increased approximately 2.90 times.
- Throughput improved by approximately 189.8%.
- Average response time decreased by approximately 65.9%.
- p95 response time decreased by approximately 75.7%.

## 6. Observations

- All 118,147 HTTP requests completed successfully.
- No HTTP request failures were recorded.
- Redis served repeated requests for the same vehicle ID.
- The warm-cache workload significantly reduced the need to retrieve the same vehicle data from PostgreSQL repeatedly.

## 7. Conclusion

Redis caching demonstrated a significant performance improvement under the tested warm-cache workload.

ScaleGate achieved approximately 3,938 requests per second compared with approximately 1,359 requests per second in the initial database-backed baseline.

This result demonstrates the potential benefit of caching frequently accessed vehicle information.

## 8. Limitations and Future Testing

- This was a local development benchmark, not a production benchmark.
- The test used only one vehicle ID.
- The cache was populated before the test began.
- The workload primarily measured cache-hit performance.
- Cache misses, multiple vehicle IDs, and realistic traffic distributions have not yet been evaluated.
- Results represent one 30-second test run and may vary with system conditions.
- Further testing is required before making claims about maximum system capacity.