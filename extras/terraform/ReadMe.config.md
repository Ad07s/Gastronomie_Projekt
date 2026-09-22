## Requirements

| Name | Version |
| ---- | ------- |
| <a name="requirement_terraform"></a> [terraform](#requirement\_terraform) | 1.15.0 |
| <a name="requirement_helm"></a> [helm](#requirement\_helm) | 3.1.1 |
| <a name="requirement_kubernetes"></a> [kubernetes](#requirement\_kubernetes) | 3.0.1 |

## Providers

| Name | Version |
| ---- | ------- |
| <a name="provider_helm"></a> [helm](#provider\_helm) | 3.1.1 |

## Modules

No modules.

## Resources

| Name | Type |
| ---- | ---- |
| [helm_release.gastronomie](https://registry.terraform.io/providers/hashicorp/helm/3.1.1/docs/resources/release) | resource |

## Inputs

| Name | Description | Type | Default | Required |
| ---- | ----------- | ---- | ------- | :------: |
| <a name="input_dev_values"></a> [dev\_values](#input\_dev\_values) | Pfad zur YAML-Datei mit Werten fuer Development | `string` | `"dev/gastronomie.yaml"` | no |
| <a name="input_helm_chart"></a> [helm\_chart](#input\_helm\_chart) | Pfad zum lokalen Helm-Chart | `string` | `"../helm/gastronomie"` | no |
| <a name="input_helm_chart_version"></a> [helm\_chart\_version](#input\_helm\_chart\_version) | Version des Helm-Charts | `string` | `"2026.4.1"` | no |
| <a name="input_helm_release"></a> [helm\_release](#input\_helm\_release) | Name fuer das Helm-Release | `string` | `"gastronomie"` | no |
| <a name="input_namespace"></a> [namespace](#input\_namespace) | Namespace fuer das Deployment | `string` | `"acme"` | no |
| <a name="input_timeout_app"></a> [timeout\_app](#input\_timeout\_app) | Timeout fuer das Ausrollen | `number` | `300` | no |

## Outputs

No outputs.