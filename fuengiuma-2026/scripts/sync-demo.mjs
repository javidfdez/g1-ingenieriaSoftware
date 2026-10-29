import {mkdir,readFile,readdir,writeFile} from 'node:fs/promises';
import {fileURLToPath} from 'node:url';
import path from 'node:path';
const root=fileURLToPath(new URL('../',import.meta.url));
const source=path.join(root,'src/main/resources/static'),target=path.join(root,process.argv.includes('--shared-repo')?'../docs':'docs');
const check=process.argv.includes('--check');
if(!check)await mkdir(target,{recursive:true});
for(const file of await readdir(source)){
  let contents=await readFile(path.join(source,file),'utf8');
  if(file==='index.html')contents=contents.replace('content="server"','content="demo"');
  if(check){let actual='';try{actual=await readFile(path.join(target,file),'utf8');}catch{}if(actual!==contents)throw new Error(`Demo desactualizada: ${file}. Ejecuta node scripts/sync-demo.mjs`);}
  else await writeFile(path.join(target,file),contents);
}
if(!check)await writeFile(path.join(target,'.nojekyll'),'');
console.log(check?'Demo sincronizada.':'Demo generada en docs/.');
