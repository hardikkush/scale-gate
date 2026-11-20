# Redis Cold-Cache Benchmark — V1

## 1. Objective

Measure application performance when Redis caching is enabled but the cache starts empty.

The purpose is to observe the behavior of cache misses, database reads, and cache population under concurrent requests.

## 2. Test Environment

| Parameter | Configuration |
|---|---|
| Application | Spring Boot |
| Cache | Redis |
| Database | PostgreSQL |
| Load testing tool | k6 v2.4.0 |
| Virtual users | 5 |
| Total iterations | 10,000 |
| Unique vehicle IDs | 6–10,005 |
| Cache TTL | 1 minute |
| Endpoint | GET `/api/v1/vehicles/{id}` |

## 3. Test Methodology

1. Start the application using the `dev` profile.
2. Confirm Redis is empty before the test (`DBSIZE = 0`).
3. Execute `cold-cache-10000.js`.
4. Each iteration requests a unique vehicle ID.
5. Each iteration sends exactly one HTTP request.
6. Check Redis after the benchmark to observe cache population.

No intentional sleep was added between requests.

## 4. Benchmark Results

### Run 1

| Metric | Result |
|---|---:|
| Total requests | 10,000 |
| Virtual users | 5 |
| Duration | 11.8 seconds |
| Throughput | 849.83 RPS |
| Average latency | 5.35 ms |
| Median latency | 3.86 ms |
| p90 latency | 8.59 ms |
| p95 latency | 11.08 ms |
| Maximum latency | 1.06 seconds |
| HTTP failures | 0 |

Redis key count observed after the run: **0**.

### Run 2

| Metric | Result |
|---|---:|
| Total requests | 10,000 |
| Virtual users | 5 |
| Duration | 5.7 seconds |
| Throughput | 1,750.62 RPS |
| Average latency | 2.32 ms |
| Median latency | 2.01 ms |
| p90 latency | 4.01 ms |
| p95 latency | 4.65 ms |
| Maximum latency | 51.03 ms |
| HTTP failures | 0 |

Redis key count observed after the run: **10,000**.

## 5. Run Comparison

| Metric | Run 1 | Run 2 |
|---|---:|---:|
| Throughput | 849.83 RPS | 1,750.62 RPS |
| Average latency | 5.35 ms | 2.32 ms |
| p95 latency | 11.08 ms | 4.65 ms |
| HTTP failures | 0% | 0% |
| Redis keys observed | 0 | 10,000 |

The two runs produced different performance results. The reason for this variation has not yet been established. These results should not be treated as a stable performance average.

## 6. Observations

- Both runs completed all 10,000 requests successfully.
- No HTTP failures were reported.
- The second run confirmed that 10,000 vehicle cache entries were present in Redis after the test.
- The test used unique vehicle IDs, so repeated requests to the same vehicle were avoided.
- The one-minute cache TTL may affect how many entries remain when Redis is inspected after a test.

## 7. Limitations

- This is a local development environment benchmark.
- Only one GET endpoint was tested.
- Only five virtual users were used.
- The test was a fixed 10,000-iteration batch, not a sustained fixed-duration workload.
- The first run's Redis key count was zero; the cause was not established.
- Cache population after the second run was verified, but individual cache misses were not independently instrumented.
- PostgreSQL, Redis, and application resource utilization were not separately measured.

These results should not be interpreted as production capacity.

## 8. Conclusion

The cold-cache benchmark completed successfully in both runs, with zero HTTP failures.

The second run demonstrated that the application populated Redis with 10,000 vehicle cache entries while processing unique vehicle requests.

This establishes an initial cold-cache benchmark reference for future comparisons with warm-cache performance and higher-concurrency tests.