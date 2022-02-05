<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Drug;
use App\Models\User;
use Illuminate\Http\Request;

use PDF;

class PatientController extends Controller
{
    public function index(Request $request, User $user){
        $user = $user->whereIn('role', ['2'])->when($request->get('q'), function ($query) use ($request){
            $query->where('name', 'like', "%{$request->get('q')}%")
                ->OrWhere('id', 'like', "%{$request->get('q')}%");
        })->orderBy('id','desc')->paginate(50);

        return response()->json($user, 200);
    }

    public function patient(User $user)
    {
        $patient = $user->whereIn('role', ['2'])->get();

        $pdf = PDF::loadview('patient', [
            'patient' => $patient
        ]);

        return $pdf->download('report_patient.pdf');
    }

    public function drug(Drug $drug)
    {
        $drug = $drug->get();

        $pdf = PDF::loadview('drug', [
            'drug' => $drug
        ]);

        return $pdf->download('report_drug.pdf');
    }

}
