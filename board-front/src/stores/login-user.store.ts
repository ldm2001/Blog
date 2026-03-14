import { User } from 'types/interface';
import { create } from 'zustand';

interface LoginUserStore {
    loginUser: User | null;
    accessToken: string | null;
    setLoginUser: (loginUser: User) => void;
    resetLoginUser: () => void;
    setAccessToken: (token: string | null) => void;
};

const useLoginUserStore = create<LoginUserStore>(set => ({
    loginUser: null,
    accessToken: null,
    setLoginUser: loginUser => set(state => ({...state, loginUser})),
    resetLoginUser: () => set(state => ({ ...state, loginUser: null, accessToken: null })),
    setAccessToken: (accessToken) => set(state => ({...state, accessToken}))
}));

export default useLoginUserStore;
