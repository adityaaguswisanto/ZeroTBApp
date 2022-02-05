<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        table {
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 10px;
        }
        th {
            background-color: #000000;
            color: white;
        }
    </style>
</head>
<body>
    <center><h1>Semua Laporan Obat Pasien</h1></center>
    <table border="1" align="center">
        <tr>
            <th>No. Pasien</th>
            <th>Nama Pasien</th>
            <th>Puskesmas</th>
            <th>Obat</th>
            <th>Dokter</th>
        </tr>
        @foreach ($drug as $item)
            <tr>
                <td align="center">{{ $item->user }}</td>
                <td align="center">{{ $item->patient }}</td>
                <td align="center">{{ $item->medical }}</td>
                <td align="center">{{ $item->drug }}</td>
                <td align="center">{{ $item->doctor }}</td>
            </tr>
        @endforeach
    </table>
</body>
</html>