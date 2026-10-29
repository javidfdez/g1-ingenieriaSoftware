/* Fictional demonstration data, shared by the offline prototype. */
(() => {
  const date = (offset=0) => { const d=new Date(); d.setDate(d.getDate()+offset); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`; };
  const locations=[['FUENGIROLA','Fuengirola'],['MIJAS','Mijas Costa'],['BENALMADENA','Benalmádena'],['TORREMOLINOS','Torremolinos'],['CAMPUS_TEATINOS','Campus de Teatinos'],['CAMPUS_EL_EJIDO','Campus de El Ejido']].map(([id,name])=>({id,name}));
  function create(){
    const users=[['Álex Ruiz','alex@uma.es','Estudio Informática. Me gusta organizar el viaje con tiempo y llegar a clase sin prisas.'],['Lucía Martín','lucia@uma.es','Salgo desde Fuengirola hacia Teatinos. Música tranquila y puntualidad.'],['Nora Santos','nora@uma.es','Comparto los trayectos de la mañana. Nos vemos en el campus.'],['Hugo Vega','hugo@uma.es','Voy a El Ejido varios días a la semana. Podemos concretar la recogida por el chat.'],['Irene Castro','irene@uma.es','Desde la costa a clase, con buena compañía.']].map(([name,email,bio],i)=>({id:i+1,name,email,bio,seed:true}));
    const tomorrow=date(1), yesterday=date(-1);
    const rows=[
      [2,'FUENGIROLA','CAMPUS_TEATINOS',tomorrow,'07:40','08:20',3.5,3,'Seat Ibiza azul','Nos encontramos en la estación de Fuengirola. Hay espacio para la mochila.'],
      [3,'FUENGIROLA','CAMPUS_TEATINOS',tomorrow,'08:15','08:50',3,2,'Toyota Yaris blanco','Salida desde Los Boliches. Llegamos a la zona de la biblioteca.'],
      [4,'FUENGIROLA','CAMPUS_EL_EJIDO',tomorrow,'08:00','08:50',4,3,'Renault Clio gris','Punto de encuentro junto a la estación.'],
      [5,'MIJAS','CAMPUS_TEATINOS',tomorrow,'07:50','08:25',2.5,3,'Hyundai i20 blanco','Recogida en Las Lagunas, junto al parque. Gastos compartidos por persona.'],
      [2,'CAMPUS_TEATINOS','FUENGIROLA',tomorrow,'14:30','15:10',3.5,3,'Seat Ibiza azul','La vuelta sale desde la Facultad de Ciencias.'],
      [1,'FUENGIROLA','CAMPUS_TEATINOS',tomorrow,'09:00','09:40',3,2,'Ford Fiesta rojo','Puedo recoger cerca de la estación. Confirmamos el punto en el chat.'],
      [2,'FUENGIROLA','CAMPUS_TEATINOS',yesterday,'07:45','08:25',3.5,3,'Seat Ibiza azul','Viaje de ejemplo ya finalizado.']
    ];
    return {version:1,currentId:null,nextId:50,users,trips:rows.map(([driverId,origin,destination,date,departure,arrival,price,capacity,car,notes],i)=>({id:i+1,driverId,origin,destination,date,departure,arrival,price,capacity,car,notes,cancelled:false})),bookings:[{id:1,tripId:6,passengerId:3,seats:1,status:'PENDING'},{id:2,tripId:7,passengerId:1,seats:1,status:'ACCEPTED'},{id:3,tripId:7,passengerId:3,seats:1,status:'ACCEPTED'},{id:4,tripId:7,passengerId:4,seats:1,status:'ACCEPTED'},{id:5,tripId:1,passengerId:1,seats:1,status:'ACCEPTED'}],reviews:[{id:1,bookingId:3,authorId:3,targetId:2,rating:5,comment:'Puntual y muy fácil coordinar la recogida.',role:'DRIVER'},{id:2,bookingId:4,authorId:4,targetId:2,rating:4,comment:'Buen trayecto y buena conversación.',role:'DRIVER'},{id:3,bookingId:3,authorId:2,targetId:3,rating:5,comment:'Todo perfecto, estaba en el punto a la hora.',role:'PASSENGER'}],messages:[{id:1,tripId:1,authorId:2,text:'¡Hola! Salimos por la entrada principal de la estación. Nos vemos mañana.',time:'18:05'}]};
  }
  globalThis.FuengiSeed={date,locations,create};
})();
