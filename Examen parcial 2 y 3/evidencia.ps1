# Evidencia de funcionamiento de ParkManager.
# 1) En otra terminal: mvn spring-boot:run   (esperar "Started ParkManagerApplication")
# 2) En esta carpeta:  powershell -ExecutionPolicy Bypass -File .\evidencia.ps1
$base = "http://localhost:8080"
$salida = Join-Path $PSScriptRoot "evidencia.txt"
$lineas = New-Object System.Collections.ArrayList

function Llamar($metodo, $ruta, $cuerpo) {
    $p = @{ Uri = "$base$ruta"; Method = $metodo; UseBasicParsing = $true }
    if ($cuerpo) { $p.Body = $cuerpo; $p.ContentType = "application/json" }
    $codigo = 0; $texto = ""
    try {
        $r = Invoke-WebRequest @p
        $codigo = [int]$r.StatusCode
        $texto = $r.Content
    } catch {
        $resp = $_.Exception.Response
        if ($resp) { $codigo = [int]$resp.StatusCode }
        if ($_.ErrorDetails -and $_.ErrorDetails.Message) {
            $texto = $_.ErrorDetails.Message
        } elseif ($resp -and $resp.GetType().GetMethod("GetResponseStream")) {
            $sr = New-Object System.IO.StreamReader($resp.GetResponseStream())
            $texto = $sr.ReadToEnd()
        } else {
            $texto = $_.Exception.Message
        }
    }
    $linea = "$metodo $ruta $cuerpo`n   -> HTTP $codigo $texto"
    Write-Host $linea
    [void]$lineas.Add($linea)
}

Llamar "PUT"    "/api/descuentos/ALUMNO" '{"porcentaje":20}'
Llamar "POST"   "/api/compras" '{"tipo":"ALUMNO","cantidad":3}'
Llamar "POST"   "/api/compras" '{"tipo":"GENERAL","cantidad":1}'
Llamar "DELETE" "/api/descuentos/ALUMNO" ''
Llamar "DELETE" "/api/descuentos/ALUMNO" ''
Llamar "POST"   "/api/compras" '{"tipo":"ALUMNO","cantidad":1}'
Llamar "PUT"    "/api/descuentos/PROFESOR" '{"porcentaje":100}'
Llamar "PUT"    "/api/descuentos/PROFESOR" '{"porcentaje":150}'
Llamar "PUT"    "/api/descuentos/PROFESOR" '{"porcentaje":-5}'
Llamar "POST"   "/api/compras" '{"tipo":"PASE_ANUAL","cantidad":0}'
Llamar "POST"   "/api/compras" '{"tipo":"PASE_ANUAL","cantidad":11}'
Llamar "POST"   "/api/compras" '{"tipo":"VIP","cantidad":1}'
Llamar "PUT"    "/api/descuentos/VIP" '{"porcentaje":10}'
Llamar "POST"   "/api/compras" '{"tipo":"GENERAL"}'
Llamar "POST"   "/api/compras" '{"tipo":"GENERAL","cantidad":'
Llamar "POST"   "/api/compras" '{"tipo":"PASE_ANUAL","cantidad":2}'

$lineas | Out-File -FilePath $salida -Encoding utf8
Write-Host "`nResultados guardados en $salida"
