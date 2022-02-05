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
    <center><h1>Semua Laporan Pasien</h1></center>
    <table border="1" align="center">
        <tr>
            <th>No. Pasien</th>
            <th>Nama</th>
            <th>Gender</th>
            <th>Puskesmas</th>
            <th>Handphone</th>
        </tr>
        @foreach ($patient as $item)
            <tr>
                <td align="center">{{ $item->id }}</td>
                <td align="center">{{ $item->name }}</td>
                <td align="center">{{ $item->gender }}</td>
                <td align="center">{{ $item->medical }}</td>
                <td align="center">{{ $item->hp }}</td>
            </tr>
        @endforeach
    </table>
</body>
</html>