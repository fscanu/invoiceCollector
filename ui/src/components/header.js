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
import app, { Component, on } from 'apprun';
var headerComponent = /** @class */ (function (_super) {
    __extends(headerComponent, _super);
    function headerComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.state = 'header';
        _this.view = function (state) {
            var user = state.user;
            return app.createElement("nav", { className: "navbar navbar-expand-md navbar-dark bg-dark mb-4" },
                app.createElement("a", { className: "navbar-brand", href: "#" }, "Top navbar"),
                app.createElement("button", { className: "navbar-toggler", type: "button", "data-toggle": "collapse", "data-target": "#navbarCollapse", "aria-controls": "navbarCollapse", "aria-expanded": "false", "aria-label": "Toggle navigation" },
                    app.createElement("span", { className: "navbar-toggler-icon" })),
                app.createElement("div", { className: "collapse navbar-collapse", id: "navbarCollapse" },
                    app.createElement("ul", { className: "navbar-nav mr-auto" },
                        app.createElement("li", { className: "nav-item active" },
                            app.createElement("a", { className: "nav-link", href: "#home" },
                                "Home ",
                                app.createElement("span", { className: "sr-only" }, "(current)"))),
                        !user && app.createElement("li", {className: "nav-item"},
                            app.createElement("a", { className: "nav-link", href: "#signin" }, "SignIn")),
                        user && app.createElement("li", {className: "nav-item"},
                        app.createElement("a", {className: "nav-link", href: "#welcome"},
                            "Welcome ",
                            user.username)),
                        !user && app.createElement("li", {className: "nav-item"},
                            app.createElement("a", { className: "nav-link", href: "#signup" }, "SignUp"))),
                    app.createElement("form", { className: "form-inline mt-2 mt-md-0" },
                        app.createElement("input", { className: "form-control mr-sm-2", type: "text", placeholder: "Search", "aria-label": "Search" }),
                        app.createElement("button", { className: "btn btn-outline-success my-2 my-sm-0", type: "submit" }, "Search"))));
        };
        _this.update = {
            '//': function (state) { return state; },
        };
        _this.setUser = function (state, user) { return (__assign({}, state, { user: user })); };
        return _this;
    }
    __decorate([
        on('/set-user')
    ], headerComponent.prototype, "setUser", void 0);
    return headerComponent;
}(Component));
export default headerComponent;
//# sourceMappingURL=header.js.map