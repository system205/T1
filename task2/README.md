# Features
## GET
- /custom-metrics **➡** returns all metrics
- /custom-metrics?tag=existing-tag **➡** returns the metric with the tag or 404

## POST
- /metrics **➡** take an array of Metric objects **➡** overwrites (and appends) given metrics