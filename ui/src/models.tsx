
export interface IUser {
    username: string;
    bio: string;
    image: string;
  }

  export interface IProfile extends IUser {
    email: string;
    token: string;
  }