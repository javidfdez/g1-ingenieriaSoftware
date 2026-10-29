/* Browser-only simulation. Real authentication and authorization live in Spring. */
(() => {
  class DemoError extends Error { constructor(status,message){super(message);this.status=status;} }
  function createDemo(storage=globalThis.localStorage,cryptoProvider=globalThis.crypto){
    const key='fuengiuma-demo-v1'; let db;
    try { db=JSON.parse(storage.getItem(key)); if(db?.version!==1) db=null; } catch { db=null; }
    if(!db) db=FuengiSeed.create();
    const save=()=>{try{storage.setItem(key,JSON.stringify(db));}catch{ /* session still works if storage is unavailable */ }};
    const fail=(status,message)=>{throw new DemoError(status,message);};
    const need=(condition,status,message)=>{if(!condition)fail(status,message);};
    const user=()=>{const u=db.users.find(x=>x.id===db.currentId);need(u,401,'Inicia sesión para continuar.');return u;};
    const trip=id=>{const t=db.trips.find(x=>x.id===Number(id));need(t,404,'El viaje no existe.');return t;};
    const active=b=>b&&['PENDING','ACCEPTED'].includes(b.status);
    const future=t=>new Date(`${t.date}T${t.departure}`).getTime()>Date.now();
    const checkFuture=t=>{need(!t.cancelled,409,'Este viaje se ha cancelado.');need(future(t),409,'El viaje ya ha comenzado.');};
    const available=t=>t.capacity-db.bookings.filter(b=>b.tripId===t.id&&b.status==='ACCEPTED').reduce((n,b)=>n+b.seats,0);
    const ownBooking=t=>db.bookings.find(b=>b.tripId===t.id&&b.passengerId===db.currentId);
    const isReviewed=b=>db.reviews.some(r=>r.bookingId===b.id&&r.authorId===db.currentId);
    const canReview=b=>{const t=trip(b.tripId);return (b.passengerId===db.currentId||t.driverId===db.currentId)&&b.status==='ACCEPTED'&&!t.cancelled&&new Date(`${t.date}T${t.arrival}`)<new Date()&&!isReviewed(b);};
    function profile(id,privateView=false){const u=db.users.find(x=>x.id===id);need(u,404,'El perfil no existe.');const all=db.reviews.filter(r=>r.targetId===id),drivers=all.filter(r=>r.role==='DRIVER'),passengers=all.filter(r=>r.role==='PASSENGER');const avg=rs=>rs.length?rs.reduce((n,r)=>n+r.rating,0)/rs.length:0;
      return {id:u.id,name:u.name,bio:u.bio,rating:avg(drivers),reviewCount:drivers.length,passengerRating:avg(passengers),passengerReviewCount:passengers.length,reviews:all.map(r=>({author:db.users.find(u=>u.id===r.authorId).name,rating:r.rating,comment:r.comment,role:r.role})),...(privateView?{email:u.email}:{})};}
    const bookingView=b=>({id:b.id,passenger:profile(b.passengerId),seats:b.seats,status:b.status,canReview:canReview(b)});
    const minutes=time=>Number(time.slice(0,2))*60+Number(time.slice(3,5));
    function view(t){const b=ownBooking(t);return {...t,duration:minutes(t.arrival)-minutes(t.departure),driver:profile(t.driverId),own:t.driverId===db.currentId,future:future(t),availableSeats:available(t),booking:b?bookingView(b):null,requests:t.driverId===db.currentId?db.bookings.filter(b=>b.tripId===t.id).map(bookingView):[]};}
    function member(t){user();need(t.driverId===db.currentId||active(ownBooking(t)),403,'Solo los participantes con una solicitud activa pueden acceder a la conversación.');}
    function messages(t){member(t);return db.messages.filter(m=>m.tripId===t.id).map(m=>({id:m.id,author:profile(m.authorId).name,own:m.authorId===db.currentId,text:m.text,time:m.time}));}
    const bounded=(s,max,label,optional=false)=>{need(typeof s==='string'&&(optional||s.trim().length>0)&&s.length<=max,400,`Revisa ${label}.`);return s.trim();};
    async function hash(password,salt){need(cryptoProvider?.subtle,400,'Abre esta demo en un navegador actualizado para crear una cuenta.');const enc=new TextEncoder();const base=await cryptoProvider.subtle.importKey('raw',enc.encode(password),'PBKDF2',false,['deriveBits']);const bits=await cryptoProvider.subtle.deriveBits({name:'PBKDF2',salt:enc.encode(salt),iterations:100000,hash:'SHA-256'},base,256);return Array.from(new Uint8Array(bits),x=>x.toString(16).padStart(2,'0')).join('');}
    async function api(path,method='GET',input={}){
      const url=new URL(path,'https://fuengiuma.local'),p=url.pathname,q=url.searchParams; let result;
      if(method==='GET'&&p==='/api/bootstrap')result={today:FuengiSeed.date(),demoEnabled:true,locations:FuengiSeed.locations,me:db.currentId?profile(db.currentId,true):null};
      else if(method==='POST'&&p==='/api/auth/demo'){db.currentId=1;result=profile(1,true);}
      else if(method==='POST'&&p==='/api/auth/logout'){db.currentId=null;result={ok:true};}
      else if(method==='POST'&&p==='/api/auth/register'){
        const name=bounded(input.name,60,'tu nombre'),email=bounded(input.email,120,'el correo').toLowerCase();need(/^[^\s@]+@(?:[a-z0-9-]+\.)?uma\.es$/i.test(email),400,'Utiliza tu correo de la UMA.');need(typeof input.password==='string'&&input.password.length>=8&&input.password.length<=64,400,'La contraseña debe tener entre 8 y 64 caracteres.');need(!db.users.some(u=>u.email===email),409,'Ya existe una cuenta con ese correo.');
        const salt=Array.from(cryptoProvider.getRandomValues(new Uint8Array(16)),n=>n.toString(16).padStart(2,'0')).join('');const passwordHash=await hash(input.password,salt);const u={id:db.nextId++,name,email,bio:'',salt,passwordHash};db.users.push(u);db.currentId=u.id;result=profile(u.id,true);
      }else if(method==='POST'&&p==='/api/auth/login'){
        const u=db.users.find(u=>u.email===String(input.email).trim().toLowerCase());need(u,401,'El correo o la contraseña no son correctos.');const ok=u.seed?input.password==='DemoUMA2026!':await hash(String(input.password),u.salt)===u.passwordHash;need(ok,401,'El correo o la contraseña no son correctos.');db.currentId=u.id;result=profile(u.id,true);
      }else if(method==='PATCH'&&p==='/api/me'){const u=user();u.name=bounded(input.name,60,'tu nombre');u.bio=bounded(input.bio,300,'tu presentación',true);result=profile(u.id,true);}
      else if(method==='GET'&&/^\/api\/users\/\d+$/.test(p))result=profile(Number(p.split('/').pop()));
      else if(method==='GET'&&p==='/api/trips'){
        const seats=Number(q.get('seats')||1);need(Number.isInteger(seats)&&seats>=1&&seats<=4,400,'Selecciona entre una y cuatro plazas.');
        for(const k of ['origin','destination'])if(q.get(k))need(FuengiSeed.locations.some(l=>l.id===q.get(k)),400,'Selecciona una localidad de la lista.');
        result=db.trips.filter(t=>!t.cancelled&&future(t)&&available(t)>=seats&&(!q.get('origin')||t.origin===q.get('origin'))&&(!q.get('destination')||t.destination===q.get('destination'))&&(!q.get('date')||t.date===q.get('date'))&&(!q.get('maxPrice')||t.price<=Number(q.get('maxPrice')))&&(!q.get('departAfter')||t.departure>=q.get('departAfter'))&&(!q.get('arriveBefore')||t.arrival<=q.get('arriveBefore'))).sort((a,b)=>(a.date+a.departure).localeCompare(b.date+b.departure)).map(view);
      }else if(method==='GET'&&p==='/api/me/trips'){user();result={driving:db.trips.filter(t=>t.driverId===db.currentId).sort((a,b)=>(b.date+b.departure).localeCompare(a.date+a.departure)).map(view),passenger:db.trips.filter(t=>ownBooking(t)).sort((a,b)=>(b.date+b.departure).localeCompare(a.date+a.departure)).map(view)};}
      else if(method==='GET'&&p==='/api/conversations'){user();result=db.trips.filter(t=>t.driverId===db.currentId||active(ownBooking(t))).map(t=>({...view(t),lastMessage:db.messages.filter(m=>m.tripId===t.id).at(-1)?.text||'Coordina aquí el punto de encuentro.'}));}
      else if(method==='GET'&&/^\/api\/trips\/\d+$/.test(p))result=view(trip(p.split('/').pop()));
      else if((method==='POST'&&p==='/api/trips')||(method==='PATCH'&&/^\/api\/trips\/\d+$/.test(p))){
        user();const t=method==='PATCH'?trip(p.split('/').pop()):{id:db.nextId++,driverId:db.currentId,cancelled:false};
        need(t.driverId===db.currentId,403,'Solo el conductor puede editar este viaje.');if(method==='PATCH'){checkFuture(t);need(!db.bookings.some(b=>b.tripId===t.id&&active(b)),409,'Gestiona o cancela las solicitudes antes de editar el viaje.');}
        need(FuengiSeed.locations.some(l=>l.id===input.origin)&&FuengiSeed.locations.some(l=>l.id===input.destination),400,'Selecciona un origen y un destino de la lista.');need(input.origin.startsWith('CAMPUS_')!==input.destination.startsWith('CAMPUS_'),400,'El trayecto debe conectar la costa con un campus de la UMA.');
        need(/^\d{4}-\d{2}-\d{2}$/.test(input.date)&&/^\d{2}:\d{2}$/.test(input.departure)&&/^\d{2}:\d{2}$/.test(input.arrival),400,'Revisa la fecha y los horarios.');const departure=new Date(`${input.date}T${input.departure}`),arrival=new Date(`${input.date}T${input.arrival}`);need(departure>new Date()&&arrival>departure&&(arrival-departure)<18000000,400,'Revisa la fecha y las horas: el viaje debe ser futuro y durar menos de cinco horas.');
        need(Number.isInteger(input.capacity)&&input.capacity>=1&&input.capacity<=4&&Number.isFinite(input.price)&&input.price>=0&&input.price<=25&&Math.abs(input.price*100-Math.round(input.price*100))<.00001,400,'Revisa el precio y las plazas.');
        const car=bounded(input.car,80,'el coche'),notes=bounded(input.notes||'',500,'las indicaciones',true);Object.assign(t,input,{car,notes});if(method==='POST')db.trips.push(t);result=view(t);
      }else if(method==='DELETE'&&/^\/api\/trips\/\d+$/.test(p)){user();const t=trip(p.split('/').pop());need(t.driverId===db.currentId,403,'Solo el conductor puede cancelar este viaje.');checkFuture(t);t.cancelled=true;db.bookings.filter(b=>b.tripId===t.id&&active(b)).forEach(b=>b.status='CANCELLED');result={ok:true};}
      else if(method==='POST'&&/^\/api\/trips\/\d+\/bookings$/.test(p)){user();const t=trip(p.split('/')[3]);checkFuture(t);need(t.driverId!==db.currentId,400,'Ya eres el conductor de este viaje.');need(Number.isInteger(input.seats)&&input.seats>=1&&input.seats<=4,400,'Revisa el número de plazas.');need(available(t)>=input.seats,409,'Ya no quedan suficientes plazas.');let b=ownBooking(t);need(!active(b),409,'Ya tienes una solicitud en este viaje.');if(!b){b={id:db.nextId++,tripId:t.id,passengerId:db.currentId};db.bookings.push(b);}b.seats=input.seats;b.status='PENDING';result=view(t);}
      else if(method==='PATCH'&&/^\/api\/bookings\/\d+$/.test(p)){user();const b=db.bookings.find(b=>b.id===Number(p.split('/').pop()));need(b,404,'La solicitud no existe.');const t=trip(b.tripId);checkFuture(t);const action=input.action;
        if(['accept','reject','remove'].includes(action)){need(t.driverId===db.currentId,403,'Solo el conductor puede gestionar las solicitudes.');need(b.status===(action==='remove'?'ACCEPTED':'PENDING'),409,'Esta solicitud ya se ha gestionado.');if(action==='accept')need(available(t)>=b.seats,409,'No quedan plazas suficientes.');b.status={accept:'ACCEPTED',reject:'REJECTED',remove:'CANCELLED'}[action];}
        else if(action==='cancel'){need(b.passengerId===db.currentId,403,'Solo el pasajero puede cancelar su solicitud.');need(active(b),409,'La solicitud ya está cerrada.');b.status='CANCELLED';}else fail(400,'La acción no es válida.');result=view(t);
      }else if(['GET','POST'].includes(method)&&/^\/api\/trips\/\d+\/messages$/.test(p)){const t=trip(p.split('/')[3]);member(t);if(method==='POST'){need(!t.cancelled,409,'El viaje está cancelado.');db.messages.push({id:db.nextId++,tripId:t.id,authorId:db.currentId,text:bounded(input.text,1000,'el mensaje'),time:new Date().toLocaleTimeString('es-ES',{hour:'2-digit',minute:'2-digit'})});}result=messages(t);}
      else if(method==='POST'&&/^\/api\/bookings\/\d+\/reviews$/.test(p)){user();const b=db.bookings.find(b=>b.id===Number(p.split('/')[3]));need(b,404,'La reserva no existe.');const t=trip(b.tripId);need(b.passengerId===db.currentId||t.driverId===db.currentId,403,'No participaste en esta reserva.');need(canReview(b),409,'Solo puedes valorar una vez un viaje confirmado y finalizado.');need(Number.isInteger(input.rating)&&input.rating>=1&&input.rating<=5,400,'Selecciona entre una y cinco estrellas.');const targetId=b.passengerId===db.currentId?t.driverId:b.passengerId;db.reviews.push({id:db.nextId++,bookingId:b.id,authorId:db.currentId,targetId,rating:input.rating,comment:bounded(input.comment,500,'el comentario'),role:b.passengerId===db.currentId?'DRIVER':'PASSENGER'});result=profile(targetId);}
      else fail(404,'Esta opción no está disponible.');
      if(method!=='GET')save();return structuredClone(result);
    }
    save();return {api,reset(){db=FuengiSeed.create();save();}};
  }
  globalThis.FuengiDemo={create:createDemo,DemoError};
})();
