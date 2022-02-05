<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Consult;
use App\Models\User;
use Illuminate\Http\Request;

class ConsultController extends Controller
{
    public function store(Request $request, Consult $consult)
    {
        $request->validate([
            'patient'   => 'required', 
            'consult'   => 'required',
            'doctor'    => 'required',
        ]);

        $consult->create([
            'patient'   => $request->patient, 
            'consult'   => $request->consult,
            'doctor'    => $request->doctor,
        ]);

        return response()->json([
            'message' => 'Berhasil input konsultasi...'
        ],201);
    }

    public function index(Consult $consult){

        $consult = $consult->orderBy('id_consult', 'desc')->paginate(50);
        
        return response()->json($consult, 200);
    }

    public function destroy(Consult $consult, $id)
    {
        $consult = $consult->find($id);
        $consult->delete();

        return response()->json([
            'message' => 'Konsultasi dihapus...'
        ], 200);
    }

}
