<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Drug;
use App\Models\Fcm;
use Illuminate\Http\Request;

class DrugController extends Controller
{
    public function store(Request $request, Drug $drug, Fcm $fcm)
    {
        $request->validate([
            'drug'      => 'required',
            'hour'      => 'required',
            'user'      => 'required',
            'medical'   => 'required',
            'patient'   => 'required',
            'doctor'    => 'required'
        ]);

        $drug->create([
            'drug'      => $request->drug,
            'hour'      => $request->hour,
            'user'      => $request->user,
            'medical'   => $request->medical,
            'patient'   => $request->patient,
            'doctor'    => $request->doctor
        ]);

        //send fcm
        $fcm = $fcm->where('user', $request->user)->value('fcm');

        sendNotification($fcm, "Hai, Obatmu sudah terdaftar nih", "Masuk aplikasi yuk untuk melihat jadwal obatmu");

    }

    public function index(Request $request, Drug $drug){
        $drug = $drug->when($request->get('q'), function ($query) use ($request){
            $query->where('user', 'like', "%{$request->get('q')}%");
        })->orderBy('id_drug','desc')->paginate(50);

        return response()->json($drug, 200);
    }

    public function destroy(Drug $drug, Fcm $fcm, $id)
    {
        $drug = $drug->find($id);
        $drug->delete();

        $fcm = $fcm->where('user', $id)->value('fcm');

        sendNotification($fcm, "Yahhh...., Obatmu sudah dihapus", "Maaf... telah menghapus obatmu");

    }

    public function update(Request $request, Drug $drug, Fcm $fcm, $id)
    {
        $request->validate([
            'drug'      => 'required',
            'hour'      => 'required',
            'user'      => 'required',
            'medical'   => 'required',
            'patient'   => 'required',
            'doctor'    => 'required'
        ]);

        $drug = $drug->find($id);
        $drug->update([
            'drug'      => $request->drug,
            'hour'      => $request->hour,
            'user'      => $request->user,
            'medical'   => $request->medical,
            'patient'   => $request->patient,
            'doctor'    => $request->doctor
        ]);

        //send fcm
        $fcm = $fcm->where('user', $request->user)->value('fcm');

        sendNotification($fcm, "Hai, Obatmu sudah diperbaharui nih", "Masuk aplikasi yuk untuk melihat jadwal obat terbarumu");
    }

}
