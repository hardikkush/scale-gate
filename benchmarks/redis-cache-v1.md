# ScaleGate — Redis Cache Benchmark v1

**Date:** 19 November 2026  
**Test type:** Redis warm-cache benchmark  
**Environment:** Local development machine

## 1. Objective

Evaluate the performance improvement achieved by introducing Redis caching compared with the initial database-backed baseline.

The same API endpoint and k6 workload were used for the initial baseline and single-key Redis benchmark.

A subsequent multi-key benchmark was conducted to evaluate cache performance across 1,000 different vehicle records and assess repeatability across five independent runs.

## 2. Test Configuration — Single-Key Benchmark

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

## 3. Single-Key Benchmark Results

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

## 6. Single-Key Benchmark Observations

- All 118,147 HTTP requests completed successfully.
- No HTTP request failures were recorded.
- Redis served repeated requests for the same vehicle ID.
- The warm-cache workload significantly reduced the need to retrieve the same vehicle data from PostgreSQL repeatedly.

## 7. Single-Key Benchmark Conclusion

Redis caching demonstrated a significant performance improvement under the tested warm-cache workload.

ScaleGate achieved approximately 3,938 requests per second compared with approximately 1,359 requests per second in the initial database-backed baseline.

This result demonstrates the potential benefit of caching frequently accessed vehicle information.

## 8. Single-Key Benchmark Limitations

- This was a local development benchmark, not a production benchmark.
- The test used only one vehicle ID.
- The cache was populated before the test began.
- The workload primarily measured cache-hit performance.
- Cache misses, multiple vehicle IDs, and realistic traffic distributions had not yet been evaluated.
- Results represent one 30-second test run and may vary with system conditions.
- Further testing is required before making claims about maximum system capacity.

---

## 9. Multi-Key Redis Cache Benchmark — Five-Run Repeatability Test

### Objective

Evaluate Redis cache performance when repeatedly requesting different vehicle records rather than repeatedly accessing a single vehicle ID.

The purpose of this test was to assess performance across a larger dataset and determine whether the observed results remained consistent across multiple independent runs.

### Test Configuration

| Parameter | Value |
|---|---|
| Endpoint | `GET /api/v1/vehicles/{id}` |
| Vehicle IDs | 6–1005 (1,000 vehicles) |
| Virtual users | 5 |
| Duration per run | 30 seconds |
| Cache state | Warm Redis cache |
| Cache TTL | 1 minute |
| Number of runs | 5 |
| Load testing tool | Grafana k6 v2.4.0 |
| Database | PostgreSQL |

**Cache preparation:** Before each benchmark run, the `warm-vehicle-cache.js` script requested all 1,000 vehicle records to populate Redis.

Each warm-up completed with 1,000 successful requests and zero failures.

The `vehicle-cache-multi-key.js` script then generated requests using randomly selected vehicle IDs from the 1,000-record dataset.

### Results

| Run | Throughput (RPS) | Average Latency | p95 Latency | HTTP Failures |
|---|---:|---:|---:|---:|
| 1 | 4,480.66 | 0.917 ms | 1.37 ms | 0% |
| 2 | 4,135.74 | 1.02 ms | 1.55 ms | 0% |
| 3 | 4,740.31 | 0.896 ms | 1.23 ms | 0% |
| 4 | 4,739.66 | 0.887 ms | 1.21 ms | 0% |
| 5 | 4,206.25 | 1.00 ms | 1.53 ms | 0% |

### Aggregate Results

| Metric | Result |
|---|---:|
| Average throughput | **4,460.52 RPS** |
| Average response latency | **0.944 ms** |
| Mean of run-level p95 latencies | **1.378 ms** |
| HTTP failure rate | **0%** |
| Total successful requests | **671,103** |
| Total failed requests | **0** |

### Observations

- All five benchmark runs completed successfully.
- The application sustained an average throughput of approximately 4,460 requests per second.
- Average response latency remained below 1 ms across the five runs.
- The mean of the run-level p95 latencies was approximately 1.378 ms.
- No HTTP request failures or failed checks were observed.
- Throughput varied between approximately 4,136 and 4,740 RPS across the five runs.
- The benchmark demonstrated consistent warm-cache performance across 1,000 different vehicle IDs.

### Conclusion

The multi-key Redis cache benchmark demonstrates that the application can serve a warm-cache read workload across 1,000 different vehicle records at approximately 4,460 RPS, with low response latency and no observed HTTP failures.

The results support considering Redis caching successful for this development-stage benchmark.

### Limitations

- The benchmark uses a pre-warmed cache; cold-cache behavior has not been evaluated.
- Only the vehicle lookup endpoint was tested.
- The test used five virtual users and does not establish maximum system capacity.
- The benchmark was executed locally and does not represent production infrastructure.
- Database writes, toll transactions, mixed workloads, and higher concurrency remain untested.

These results should be treated as a local development benchmark, not a production performance guarantee.

### Comparison Note

The initial Redis benchmark and the multi-key benchmark use different workloads.

The initial benchmark repeatedly accessed a single vehicle ID, while the multi-key benchmark accessed randomly selected IDs from a dataset of 1,000 vehicles.

Therefore, their throughput figures should not be treated as a direct before-and-after performance comparison. 