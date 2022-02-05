<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Fcm;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Password;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function register(Request $request, User $user)
    {
        $request->validate([
            'name'      => 'required|min:3',
            'username'  => 'required|min:3|unique:users',
            'email'     => 'required|min:3|unique:users',
            'gender'    => 'required|min:1',
            'hp'        => 'required|max:15',
            'password'  => 'required|min:3',
            'role'      => 'required|min:1',
        ]);

        $user->create([
            'name'      => $request->name,
            'username'  => $request->username,
            'email'     => $request->email,
            'gender'    => $request->gender,
            'hp'        => $request->hp,
            'medical'   => $request->medical,
            'password'  => bcrypt($request->password),
            'role'      => $request->role,
        ]);

        return response()->json([
            'message' => 'Berhasil membuat akun...'
        ], 201);

    }

    public function login(Request $request, User $user, Fcm $fcm)
    {
        $request->validate([
            'email'     => 'required',
            'password'  => 'required',
        ]);

        $login_type = filter_var($request->input('email'), FILTER_VALIDATE_EMAIL) ? 'email' : 'username';

        $request->merge([
            $login_type => $request->input('email')
        ]);

        if (!Auth::attempt($request->only($login_type, 'password'))) {
            return response()->json([
                'message'   => 'Email atau password salah, Silahkan coba lagi !',
            ], 401);
        }

        $user = $request->user();
        $token = $user->createToken(auth()->user()->name)->plainTextToken;

        //fcm token
        $fcm = $fcm->updateOrCreate([
            'fcm'     => $request->fcm,
            'user'    => auth()->user()->id,
        ]);

        return response()->json([
            'user'      => $user,
            'token'     => $token,
        ], 200);
    }

    public function logout(Request $request, Fcm $fcm)
    {
        $user = $request->user();
        $user->tokens()->delete();

        $fcm = $fcm->where('user', Auth::user()->id);
        $fcm->delete();

        return response()->json([
            'message' => 'Logout Berhasil...',
        ], 200);
    }

    public function forgot(Request $request)
    {
        $request->validate([
            'email' => 'required|email'
        ]);

        $status = Password::sendResetLink(
            $request->only('email')
        );

        if ($status == Password::RESET_LINK_SENT) {
            return [
                'message' => __($status)
            ];
        }

        throw ValidationException::withMessages([
            'email' => [trans($status)],
        ]);
    }

    public function user(User $user)
    {
        $user = $user->where('id', Auth::user()->id)->first();

        return response()->json([
            'user'  => $user
        ], 200);
    }

    public function profile(Request $request, User $user)
    {
        $request->validate([
            'profile'    => 'required'
        ]);

        $profile = $request->file('profile');
        $nprofile = time() . '.' . $profile->getClientOriginalExtension();
        $profile->move('profiles', $nprofile);

        $user = $user->find(Auth::user()->id);

        $user->update([
            'profile'        => $nprofile,
        ]);

        return response()->json([
            'message'   => 'Foto profile berhasil diubah',
        ], 200);
    }

    public function update(Request $request, User $user, $id)
    {
        $request->validate([
            'name'      => 'required|min:3',
            'username'  => 'required|min:3',
            'email'     => 'required|min:3',
            'gender'    => 'required|min:1',
            'hp'        => 'required|max:15',
        ]);

        $user = $user->find($id);
        $user->update([
            'name'      => $request->name,
            'username'  => $request->username,
            'email'     => $request->email,
            'gender'    => $request->gender,
            'hp'        => $request->hp,
        ]);

        return response()->json([
            'message' => 'Berhasil mengubah akun...'
        ], 200);

    }

    public function medical(Request $request, User $user, $id)
    {
        $request->validate([
            'medical'   => 'required|min:3',
        ]);

        $user = $user->find($id);
        $user->update([
            'medical'   => $request->medical,
        ]);

        return response()->json([
            'message' => 'Berhasil mengubah puskesmas...'
        ], 200);

    }
}
