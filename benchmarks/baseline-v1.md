# ScaleGate — Baseline Benchmark v1

**Date:** 19 November 2026  
**Test type:** Initial database-backed baseline  
**Environment:** Local development machine

## 1. Objective

Establish an initial performance baseline for ScaleGate before introducing and benchmarking Redis caching and further infrastructure optimizations.

## 2. Test Configuration

| Parameter | Value |
|---|---|
| Endpoint | `GET /api/v1/vehicles/3` |
| Virtual users | 5 |
| Test duration | 30 seconds |
| Cache configuration | Disabled (`NoOpCacheManager`) |
| Load testing tool | Grafana k6 |
| Database | PostgreSQL |

## 3. Results

| Metric | Result |
|---|---:|
| Total HTTP requests | 40,770 |
| Throughput | 1,358.92 requests/sec |
| Average response time | 3.37 ms |
| Median (p50) | 1.99 ms |
| p90 response time | 7.10 ms |
| p95 response time | 9.61 ms |
| Maximum response time | 77.82 ms |
| HTTP request failure rate | 0% |
| Check success rate | 100% |

## 4. Observations

- All 40,770 HTTP requests completed successfully.
- No HTTP request failures were recorded.
- The p95 response time was 9.61 ms.
- The application handled approximately 1,359 requests per second with 5 virtual users during this test.

## 5. Conclusion

This benchmark establishes the initial database-backed performance baseline for ScaleGate.

Future benchmarks will compare performance after introducing Redis caching, database connection pooling, load balancing, and horizontal scaling.

## 6. Important Notes

- This was a local development benchmark, not a production benchmark.
- Results represent one 30-second test run.
- Results may vary with hardware, background processes, database state, and workload.
- These results should not be interpreted as the application's maximum capacity.
- Future comparisons should use consistent workloads and test conditions.
