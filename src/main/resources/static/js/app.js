const API_BASE="http://localhost:8080";
const PRODUCTS_API=`${API_BASE}/api/products`;
const PRODUCT_SEARCH_API=k=>`${API_BASE}/api/products/search?keyword=${encodeURIComponent(k)}`;
const PRODUCT_IMAGE_API=id=>`${API_BASE}/api/product/${id}/image`;
const CART_API=`${API_BASE}/cart`;
const ORDER_CHECKOUT_API=`${API_BASE}/order/checkout`;
const ORDERS_API=`${API_BASE}/orders`;
const ORDER_API=`${API_BASE}/order`;
let currentUser=JSON.parse(localStorage.getItem("currentUser")||"null"),allProducts=[];
const $=id=>document.getElementById(id);

function toast(m){const e=$("toast");e.textContent=m;e.classList.add("show");setTimeout(()=>e.classList.remove("show"),2200)}
function formatPrice(v){return new Intl.NumberFormat("en-IN",{style:"currency",currency:"INR",maximumFractionDigits:2}).format(Number(v)||0)}
function esc(v){return String(v??"").replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll('"',"&quot;").replaceAll("'","&#039;")}

function showLogin(){$("signupPanel").classList.add("hidden");$("loginPanel").classList.remove("hidden")}
function showSignup(){$("loginPanel").classList.add("hidden");$("signupPanel").classList.remove("hidden")}
function showStore(){$("authView").classList.add("hidden");$("navbar").classList.remove("hidden");$("storeView").classList.remove("hidden");showProducts();loadCart()}
function showAuth(){$("navbar").classList.add("hidden");$("storeView").classList.add("hidden");$("authView").classList.remove("hidden");showSignup()}
$("showLoginBtn").onclick=showLogin;$("showSignupBtn").onclick=showSignup;

$("signupForm").addEventListener("submit",async e=>{e.preventDefault();const user={uname:$("signupUname").value.trim(),name:$("signupName").value.trim(),email:$("signupEmail").value.trim(),pass:$("signupPass").value,phno:$("signupPhno").value.trim()};try{const r=await fetch(`${API_BASE}/api/signup`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(user)}),m=await r.text();if(!r.ok)return toast(m||"Signup failed");toast("Account created successfully");$("loginEmail").value=user.email;showLogin()}catch(e){toast("Backend is not reachable");console.error(e)}});

$("loginForm").addEventListener("submit",async e=>{e.preventDefault();const credentials={email:$("loginEmail").value.trim(),pass:$("loginPass").value};try{const r=await fetch(`${API_BASE}/api/login`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(credentials)});if(!r.ok)return toast(await r.text()||"Login failed");const user=await r.json();if(user.uid==null)return toast("Login succeeded but user ID was not returned.");currentUser=user;localStorage.setItem("currentUser",JSON.stringify(user));toast(`Welcome ${user.name||user.uname||"User"}`);showStore()}catch(e){toast("Backend not reachable");console.error(e)}});

async function loadProducts(){try{const r=await fetch(PRODUCTS_API);if(!r.ok)throw Error();const d=await r.json();allProducts=Array.isArray(d)?d:(d.content||[]);renderProducts(allProducts)}catch(e){$("productGrid").innerHTML=`<div class="empty"><h3>Products couldn't be loaded</h3><p>Make sure Spring Boot is running.</p></div>`}}
async function searchProducts(k){if(!k.trim())return renderProducts(allProducts);try{const r=await fetch(PRODUCT_SEARCH_API(k.trim()));if(!r.ok)throw Error();const d=await r.json();renderProducts(Array.isArray(d)?d:(d.content||[]))}catch(e){toast("Search failed")}}
function renderProducts(ps){$("resultCount").textContent=`${ps.length} product${ps.length===1?"":"s"}`;if(!ps.length){$("productGrid").innerHTML=`<div class="empty"><h3>No products found</h3></div>`;return}$("productGrid").innerHTML=ps.map(p=>{const id=p.id??p.productId,n=p.name??"Product",b=p.brand??"",c=p.category??"General",d=p.description??"",price=p.price??0;return `<article class="product-card"><div class="product-image"><img src="${PRODUCT_IMAGE_API(id)}" alt="${esc(n)}" onerror="this.style.display='none';this.nextElementSibling.style.display='block'"><div style="display:none">No image</div></div><div class="product-info"><div class="category">${esc(c)}</div><div class="product-name">${esc(n)}</div><div class="brand-line">${esc(b)}</div><div class="desc">${esc(d)}</div><div class="product-bottom"><span class="price">${formatPrice(price)}</span><button class="add-btn" onclick="addToCart(${id})">Add to cart</button></div></div></article>`}).join("")}

async function addToCart(pid){if(!currentUser?.uid)return toast("Please login first");try{const r=await fetch(`${CART_API}/add?pid=${encodeURIComponent(pid)}&uid=${encodeURIComponent(currentUser.uid)}`,{method:"POST"}),m=await r.text();if(!r.ok)return toast(m||"Could not add product");toast(m||"Added to cart");await loadCart()}catch(e){toast("Cart API is not reachable")}}
async function loadCart(){if(!currentUser?.uid)return;try{const r=await fetch(`${CART_API}/${encodeURIComponent(currentUser.uid)}`);if(!r.ok)throw Error();renderCart(await r.json())}catch(e){$("cartContent").innerHTML=`<div class="empty"><h3>Cart couldn't be loaded</h3></div>`;$("cartCount").textContent="0"}}
function renderCart(items){items=Array.isArray(items)?items:[];const count=items.reduce((s,x)=>s+Number(x.quantity||0),0);$("cartCount").textContent=count;if(!items.length){$("cartContent").innerHTML=`<div class="empty"><h2>Your ShopCart is empty</h2><p>Add products from the home page.</p></div>`;return}let total=0;const rows=items.map(x=>{const q=Number(x.quantity||0),p=Number(x.price||0);total+=q*p;return `<div class="cart-row"><div><div class="cart-name">Product #${esc(x.productId)}</div><div class="cart-meta">Unit price · ${formatPrice(p)}</div></div><strong>${formatPrice(q*p)}</strong><div class="qty"><button onclick="decreaseQuantity(${x.productId})">−</button><span>${q}</span><button onclick="increaseQuantity(${x.productId})">+</button></div><button class="remove" onclick="removeItem(${x.productId})">Remove</button></div>`}).join("");$("cartContent").innerHTML=`<div class="cart-list">${rows}</div><div class="summary"><div class="sum-line"><span>Items</span><span>${count}</span></div><div class="sum-total"><span>Order total</span><span>${formatPrice(total)}</span></div><button class="checkout-btn" onclick="checkout()">Proceed to Checkout</button></div>`}
async function cartRequest(path,method){try{const r=await fetch(`${CART_API}${path}`,{method});if(!r.ok)throw Error();await loadCart()}catch(e){toast("Cart operation failed")}}
async function increaseQuantity(pid){await cartRequest(`/incQuantity?pid=${encodeURIComponent(pid)}&uid=${encodeURIComponent(currentUser.uid)}`,"PUT")}
async function decreaseQuantity(pid){await cartRequest(`/DecQuantity?pid=${encodeURIComponent(pid)}&uid=${encodeURIComponent(currentUser.uid)}`,"DELETE")}
async function removeItem(pid){await cartRequest(`/remove?pid=${encodeURIComponent(pid)}&uid=${encodeURIComponent(currentUser.uid)}`,"DELETE")}

/* ================= CHECKOUT VIA YOUR REST API ================= */
async function checkout(){
 if(!currentUser?.uid)return toast("Please login first");
 try{
   const res=await fetch(`${ORDER_CHECKOUT_API}?uid=${encodeURIComponent(currentUser.uid)}`,{method:"POST"});
   if(!res.ok){const msg=await res.text();toast(msg||"Checkout failed");return}
   const order=await res.json();
   toast(`Order #${order.orderId} placed successfully`);
   await loadCart();
   showOrders();
 }catch(err){toast("Checkout failed");console.error(err)}
}

/* ================= ORDERS VIA YOUR REST APIs ================= */
async function loadOrders(){
 if(!currentUser?.uid)return;
 $("ordersContent").innerHTML=`<div class="empty"><h3>Loading your orders...</h3></div>`;
 try{
   const res=await fetch(`${ORDERS_API}/${encodeURIComponent(currentUser.uid)}`);
   if(!res.ok)throw Error();
   const orders=await res.json();
   renderOrders(Array.isArray(orders)?orders:[]);
 }catch(err){$("ordersContent").innerHTML=`<div class="empty"><h3>Orders couldn't be loaded</h3></div>`;console.error(err)}
}
function renderOrders(orders){
 if(!orders.length){$("ordersContent").innerHTML=`<div class="empty"><h2>You haven't placed any orders yet.</h2></div>`;return}
 $("ordersContent").innerHTML=orders.map(o=>`<div class="order-card">
 <div class="order-top"><span><b>Order #${o.orderId}</b></span><span class="order-status">${esc(o.status||"PLACED")}</span></div>
 <div class="order-info"><span>${o.orderDate?new Date(o.orderDate).toLocaleString():""}</span><strong>${formatPrice(o.totalAmount)}</strong></div>
 <button class="order-track-btn" onclick="trackOrder(${o.orderId})">Track order</button>
 <div id="tracking-${o.orderId}"></div></div>`).join("")
}
async function trackOrder(orderId){
 try{
   const res=await fetch(`${ORDER_API}/${encodeURIComponent(orderId)}`);
   if(!res.ok)throw Error();
   renderTracking(await res.json());
 }catch(err){toast("Could not load order");console.error(err)}
}
function renderTracking(o){
 const statuses=["PLACED","CONFIRMED","SHIPPED","OUT_FOR_DELIVERY","DELIVERED"];
 const current=statuses.indexOf(o.status);
 const el=$(`tracking-${o.orderId}`);
 if(!el)return;
 el.innerHTML=`<div class="tracking"><b>Order tracking</b><div class="tracking-line">${statuses.map((s,i)=>`<div class="track-step ${i<=current?"active":""}"><div class="track-dot">${i<=current?"✓":i+1}</div>${s.replaceAll("_"," ")}</div>`).join("")}</div></div>`
}

/* ================= NAVIGATION ================= */
function hidePages(){$("homeView").classList.add("hidden");$("cartView").classList.add("hidden");$("ordersView").classList.add("hidden")}
function active(id){document.querySelectorAll(".nav-link").forEach(x=>x.classList.remove("active"));$(id).classList.add("active")}
function showProducts(){hidePages();$("homeView").classList.remove("hidden");active("homeBtn");loadProducts()}
function showCart(){hidePages();$("cartView").classList.remove("hidden");active("cartBtn");loadCart()}
function showOrders(){hidePages();$("ordersView").classList.remove("hidden");active("ordersBtn");loadOrders()}
$("homeBtn").onclick=showProducts;$("cartBtn").onclick=showCart;$("ordersBtn").onclick=showOrders;
$("searchBtn").onclick=()=>searchProducts($("searchInput").value);
$("searchInput").onkeydown=e=>{if(e.key==="Enter")searchProducts(e.target.value)};
$("logoutBtn").onclick=()=>{currentUser=null;localStorage.removeItem("currentUser");showAuth();toast("Logged out")};
if(currentUser?.uid)showStore();else showAuth();