var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
import app, { Component, on } from 'apprun';
import { auth, serializeObject } from '../api';
var signupComponent = /** @class */ (function (_super) {
    __extends(signupComponent, _super);
    function signupComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.state = 'signup';
        _this.view = function (state) {
            return app.createElement("div", { class: "container" },
                app.createElement("div", { class: "row" },
                    app.createElement("div", { class: "col-lg-10 col-xl-9 mx-auto" },
                        app.createElement("div", { class: "card card-signin flex-row my-5" },
                            app.createElement("div", { class: "card-img-left d-none d-md-flex" }),
                            app.createElement("div", { class: "card-body" },
                                app.createElement("h5", { class: "card-title text-center" }, "Register"),
                                app.createElement("form", { class: "form-signin", onsubmit: function (e) { return _this.run('register', e); } },
                                    app.createElement("div", { class: "form-label-group" },
                                        app.createElement("input", { type: "text", id: "username", class: "form-control", placeholder: "Username", required: true, autofocus: true }),
                                        app.createElement("label", { for: "username" }, "Username")),
                                    app.createElement("div", { class: "form-label-group" },
                                        app.createElement("input", { type: "email", id: "email", class: "form-control", placeholder: "Email address", required: true }),
                                        app.createElement("label", { for: "email" }, "Email address")),
                                    app.createElement("hr", null),
                                    app.createElement("div", { class: "form-label-group" },
                                        app.createElement("input", { type: "password", id: "password", class: "form-control", placeholder: "Password", required: true }),
                                        app.createElement("label", { for: "password" }, "Password")),
                                    app.createElement("div", { class: "form-label-group" },
                                        app.createElement("input", { type: "password", id: "confirmPassword", class: "form-control", placeholder: "Password", required: true }),
                                        app.createElement("label", { for: "confirmPassword" }, "Confirm password")),
                                    app.createElement("button", { class: "btn btn-lg btn-primary btn-block text-uppercase", type: "submit" }, "Register"),
                                    app.createElement("a", { class: "d-block text-center mt-2 small", href: "#signin" }, "Sign In"),
                                    app.createElement("hr", { class: "my-4" }),
                                    app.createElement("button", { class: "btn btn-lg btn-google btn-block text-uppercase", type: "submit" },
                                        app.createElement("i", { class: "fab fa-google mr-2" }),
                                        " Sign up with Google"),
                                    app.createElement("button", { class: "btn btn-lg btn-facebook btn-block text-uppercase", type: "submit" },
                                        app.createElement("i", { class: "fab fa-facebook-f mr-2" }),
                                        " Sign up with Facebook")))))));
        };
        _this.register = function (state, messages) { return (__assign({}, state, { messages: messages })); };
        _this.submitRegistration = function (state, e) { return __awaiter(_this, void 0, void 0, function () {
            var session, _a, errors;
            return __generator(this, function (_b) {
                switch (_b.label) {
                    case 0:
                        _b.trys.push([0, 2, , 3]);
                        e.preventDefault();
                        return [4 /*yield*/, auth.register(serializeObject(e.target))];
                    case 1:
                        session = _b.sent();
                        app.run('/set-user', session.user);
                        app.run('route', '#home');
                        return [3 /*break*/, 3];
                    case 2:
                        _a = _b.sent();
                        errors = _a.errors;
                        return [2 /*return*/, __assign({}, state, { errors: errors })];
                    case 3: return [2 /*return*/];
                }
            });
        }); };
        _this.update = {
            '#signup': function (state) { return state; },
        };
        return _this;
    }
    __decorate([
        on('#/register')
    ], signupComponent.prototype, "register", void 0);
    __decorate([
        on('register')
    ], signupComponent.prototype, "submitRegistration", void 0);
    return signupComponent;
}(Component));
export default signupComponent;
//# sourceMappingURL=signup.js.map